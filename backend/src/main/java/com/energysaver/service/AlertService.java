package com.energysaver.service;

import com.energysaver.dto.AlertDTO;
import com.energysaver.dto.AlertResponseRequest;
import com.energysaver.entity.AlertNotification;
import com.energysaver.entity.AlertResponse;
import com.energysaver.entity.DailyConsumption;
import com.energysaver.entity.User;
import com.energysaver.entity.UserAppliance;
import com.energysaver.exception.ResourceNotFoundException;
import com.energysaver.repository.AlertNotificationRepository;
import com.energysaver.repository.AlertResponseRepository;
import com.energysaver.repository.DailyConsumptionRepository;
import com.energysaver.repository.UserApplianceRepository;
import com.energysaver.repository.UserRepository;
import com.energysaver.util.EnergyCalculator;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlertService {
    
    private final AlertNotificationRepository alertNotificationRepository;
    private final AlertResponseRepository alertResponseRepository;
    private final DailyConsumptionRepository dailyConsumptionRepository;
    private final UserApplianceRepository userApplianceRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    
    public AlertService(AlertNotificationRepository alertNotificationRepository,
                       AlertResponseRepository alertResponseRepository,
                       DailyConsumptionRepository dailyConsumptionRepository,
                       UserApplianceRepository userApplianceRepository,
                       UserRepository userRepository,
                       SimpMessagingTemplate messagingTemplate) {
        this.alertNotificationRepository = alertNotificationRepository;
        this.alertResponseRepository = alertResponseRepository;
        this.dailyConsumptionRepository = dailyConsumptionRepository;
        this.userApplianceRepository = userApplianceRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }
    
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
    
    @Scheduled(cron = "0 0 0 * * ?")
    public void detectHighConsumption() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        
        List<User> allUsers = userRepository.findAll();
        
        for (User user : allUsers) {
            List<Object[]> consumptionData = dailyConsumptionRepository
                    .findTotalConsumptionByUserAppliance(user.getId());
            
            if (consumptionData.isEmpty()) {
                continue;
            }
            
            int totalAppliances = consumptionData.size();
            int percentile75Index = (int) Math.ceil(totalAppliances * 0.25);
            
            Map<Long, Double> consumptionMap = new HashMap<>();
            for (Object[] row : consumptionData) {
                Long applianceId = ((Number) row[0]).longValue();
                Double totalWh = ((Number) row[1]).doubleValue();
                consumptionMap.put(applianceId, totalWh);
            }
            
            List<Long> topConsumerIds = consumptionData.stream()
                    .limit(percentile75Index)
                    .map(row -> ((Number) row[0]).longValue())
                    .collect(Collectors.toList());
            
            for (Long applianceId : topConsumerIds) {
                UserAppliance userAppliance = userApplianceRepository.findById(applianceId)
                        .orElse(null);
                
                if (userAppliance == null) {
                    continue;
                }
                
                Double totalConsumption = consumptionMap.get(applianceId);
                
                AlertNotification alert = new AlertNotification();
                alert.setUser(user);
                alert.setUserAppliance(userAppliance);
                alert.setAlertType("HIGH_CONSUMPTION");
                alert.setMessage(String.format(
                        "High consumption detected for %s: %.2f Wh total. Consider reducing usage.",
                        userAppliance.getEffectiveName(),
                        totalConsumption
                ));
                alert.setIsRead(false);
                
                alert = alertNotificationRepository.save(alert);
                
                AlertDTO alertDTO = convertToAlertDTO(alert);
                messagingTemplate.convertAndSend("/topic/alerts/" + user.getId(), alertDTO);
            }
        }
    }
    
    @Transactional(readOnly = true)
    public List<AlertDTO> getUserAlerts(String username) {
        User user = getUserByUsername(username);
        
        return alertNotificationRepository.findByUserId(user.getId()).stream()
                .map(this::convertToAlertDTO)
                .collect(Collectors.toList());
    }
    
    public AlertDTO markAlertAsRead(Long alertId, String username) {
        User user = getUserByUsername(username);
        AlertNotification alert = alertNotificationRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + alertId));
        
        if (!alert.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Alert not found with id: " + alertId + " for user: " + username);
        }
        
        alert.setIsRead(true);
        alert = alertNotificationRepository.save(alert);
        
        return convertToAlertDTO(alert);
    }
    
    public void respondToAlert(AlertResponseRequest request, String username) {
        User user = getUserByUsername(username);
        AlertNotification alert = alertNotificationRepository.findById(request.getAlertId())
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with id: " + request.getAlertId()));
        
        if (!alert.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Alert not found with id: " + request.getAlertId() + " for user: " + username);
        }
        
        AlertResponse response = new AlertResponse();
        response.setAlert(alert);
        response.setUserResponse(request.getUserResponse());
        if (request.getAdjustmentPercentage() != null) {
            response.setAdjustmentPercentage(BigDecimal.valueOf(request.getAdjustmentPercentage()));
        }
        alertResponseRepository.save(response);
        
        if (request.getAdjustmentPercentage() != null && request.getAdjustmentPercentage() > 0 
                && alert.getUserAppliance() != null) {
            
            UserAppliance userAppliance = alert.getUserAppliance();
            LocalDate today = LocalDate.now();
            
            List<DailyConsumption> recentConsumption = dailyConsumptionRepository
                    .findByUserIdAndDate(user.getId(), today.minusDays(1));
            
            for (DailyConsumption dc : recentConsumption) {
                if (dc.getUserAppliance().getId().equals(userAppliance.getId())) {
                    double currentHours = dc.getHoursUsed().doubleValue();
                    double reductionFactor = 1.0 - (request.getAdjustmentPercentage() / 100.0);
                    double adjustedHours = currentHours * reductionFactor;
                    
                    double ratedWatts = dc.getRatedWattsSnapshot();
                    double adjustedWh = EnergyCalculator.calculateWh(ratedWatts, adjustedHours);
                    double adjustedKwh = EnergyCalculator.calculateKwh(ratedWatts, adjustedHours);
                    
                    DailyConsumption adjustedConsumption = dailyConsumptionRepository
                            .findByUserApplianceIdAndLogDate(userAppliance.getId(), today)
                            .orElse(new DailyConsumption());
                    
                    adjustedConsumption.setUserAppliance(userAppliance);
                    adjustedConsumption.setLogDate(today);
                    adjustedConsumption.setRatedWattsSnapshot(ratedWatts);
                    adjustedConsumption.setHoursUsed(BigDecimal.valueOf(adjustedHours));
                    adjustedConsumption.setWhConsumed(BigDecimal.valueOf(adjustedWh));
                    adjustedConsumption.setKwhConsumed(BigDecimal.valueOf(adjustedKwh));
                    
                    dailyConsumptionRepository.save(adjustedConsumption);
                    break;
                }
            }
        }
        
        alert.setIsRead(true);
        alertNotificationRepository.save(alert);
    }
    
    private AlertDTO convertToAlertDTO(AlertNotification alert) {
        AlertDTO dto = new AlertDTO();
        dto.setId(alert.getId());
        dto.setUserId(alert.getUser().getId());
        dto.setUserApplianceId(alert.getUserAppliance() != null ? alert.getUserAppliance().getId() : null);
        dto.setApplianceName(alert.getUserAppliance() != null ? alert.getUserAppliance().getEffectiveName() : null);
        dto.setAlertType(alert.getAlertType());
        dto.setMessage(alert.getMessage());
        dto.setIsRead(alert.getIsRead());
        dto.setCreatedAt(alert.getCreatedAt());
        return dto;
    }
}
