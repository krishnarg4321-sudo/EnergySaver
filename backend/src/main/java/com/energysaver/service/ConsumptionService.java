package com.energysaver.service;

import com.energysaver.dto.ConsumptionDTO;
import com.energysaver.dto.ConsumptionLogRequest;
import com.energysaver.entity.DailyConsumption;
import com.energysaver.entity.UserAppliance;
import com.energysaver.exception.ResourceNotFoundException;
import com.energysaver.repository.DailyConsumptionRepository;
import com.energysaver.repository.UserApplianceRepository;
import com.energysaver.util.EnergyCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConsumptionService {
    
    private final DailyConsumptionRepository dailyConsumptionRepository;
    private final UserApplianceRepository userApplianceRepository;
    
    public ConsumptionService(DailyConsumptionRepository dailyConsumptionRepository,
                             UserApplianceRepository userApplianceRepository) {
        this.dailyConsumptionRepository = dailyConsumptionRepository;
        this.userApplianceRepository = userApplianceRepository;
    }
    
    public ConsumptionDTO logConsumption(Long userId, ConsumptionLogRequest request) {
        UserAppliance userAppliance = userApplianceRepository.findByIdAndUserId(request.getUserApplianceId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User appliance not found with id: " + request.getUserApplianceId() + " for user: " + userId));
        
        double ratedWatts = userAppliance.getEffectiveRatedWatts();
        double hoursUsed = request.getHoursUsed();
        
        double wh = EnergyCalculator.calculateWh(ratedWatts, hoursUsed);
        double kwh = EnergyCalculator.calculateKwh(ratedWatts, hoursUsed);
        
        DailyConsumption consumption = dailyConsumptionRepository
                .findByUserApplianceIdAndLogDate(request.getUserApplianceId(), request.getLogDate())
                .orElse(new DailyConsumption());
        
        consumption.setUserAppliance(userAppliance);
        consumption.setLogDate(request.getLogDate());
        consumption.setRatedWattsSnapshot(ratedWatts);
        consumption.setHoursUsed(BigDecimal.valueOf(hoursUsed));
        consumption.setWhConsumed(BigDecimal.valueOf(wh));
        consumption.setKwhConsumed(BigDecimal.valueOf(kwh));
        
        consumption = dailyConsumptionRepository.save(consumption);
        
        return convertToConsumptionDTO(consumption);
    }
    
    @Transactional(readOnly = true)
    public List<ConsumptionDTO> getUserConsumption(Long userId, LocalDate startDate, LocalDate endDate) {
        return dailyConsumptionRepository.findByUserIdAndDateRange(userId, startDate, endDate).stream()
                .map(this::convertToConsumptionDTO)
                .collect(Collectors.toList());
    }
    
    private ConsumptionDTO convertToConsumptionDTO(DailyConsumption consumption) {
        ConsumptionDTO dto = new ConsumptionDTO();
        dto.setId(consumption.getId());
        dto.setUserApplianceId(consumption.getUserAppliance().getId());
        dto.setApplianceName(consumption.getUserAppliance().getEffectiveName());
        dto.setLogDate(consumption.getLogDate());
        dto.setRatedWattsSnapshot(consumption.getRatedWattsSnapshot());
        dto.setHoursUsed(consumption.getHoursUsed().doubleValue());
        dto.setWhConsumed(consumption.getWhConsumed().doubleValue());
        dto.setKwhConsumed(consumption.getKwhConsumed());
        return dto;
    }
}
