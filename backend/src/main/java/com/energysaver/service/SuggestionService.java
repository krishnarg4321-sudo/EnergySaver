package com.energysaver.service;

import com.energysaver.dto.SuggestionDTO;
import com.energysaver.entity.User;
import com.energysaver.entity.UserAppliance;
import com.energysaver.exception.ResourceNotFoundException;
import com.energysaver.repository.DailyConsumptionRepository;
import com.energysaver.repository.UserApplianceRepository;
import com.energysaver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class SuggestionService {
    
    private final DailyConsumptionRepository dailyConsumptionRepository;
    private final UserApplianceRepository userApplianceRepository;
    private final UserRepository userRepository;
    
    public SuggestionService(DailyConsumptionRepository dailyConsumptionRepository,
                            UserApplianceRepository userApplianceRepository,
                            UserRepository userRepository) {
        this.dailyConsumptionRepository = dailyConsumptionRepository;
        this.userApplianceRepository = userApplianceRepository;
        this.userRepository = userRepository;
    }
    
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
    
    public List<SuggestionDTO> getSuggestions(String username) {
        User user = getUserByUsername(username);
        List<UserAppliance> userAppliances = userApplianceRepository.findByUserId(user.getId());
        
        if (userAppliances.isEmpty()) {
            return List.of();
        }
        
        List<Object[]> totalConsumptionData = dailyConsumptionRepository
                .findTotalConsumptionByUserAppliance(user.getId());
        
        Map<Long, Double> consumptionMap = new HashMap<>();
        for (Object[] row : totalConsumptionData) {
            Long applianceId = ((Number) row[0]).longValue();
            Double totalWh = ((Number) row[1]).doubleValue();
            consumptionMap.put(applianceId, totalWh);
        }
        
        List<SuggestionDTO> suggestions = new ArrayList<>();
        
        int totalAppliances = userAppliances.size();
        
        for (int i = 0; i < userAppliances.size(); i++) {
            UserAppliance ua = userAppliances.get(i);
            Double totalConsumption = consumptionMap.getOrDefault(ua.getId(), 0.0);
            
            double percentile = totalAppliances > 1 
                ? ((double) (totalAppliances - 1 - i) / (totalAppliances - 1)) * 100.0 
                : 50.0;
            
            SuggestionDTO suggestion = new SuggestionDTO();
            suggestion.setUserApplianceId(ua.getId());
            suggestion.setApplianceName(ua.getEffectiveName());
            suggestion.setTotalConsumption(totalConsumption);
            suggestion.setPercentile(percentile);
            
            if (totalConsumption == 0.0) {
                suggestion.setSuggestionType("CLEANUP");
                suggestion.setMessage("This appliance has no recorded consumption. Consider removing it if no longer in use.");
            } else if (percentile >= 75.0) {
                suggestion.setSuggestionType("REDUCTION");
                suggestion.setMessage(String.format("High consumption detected (%s Wh). Consider reducing usage hours or upgrading to energy-efficient model.", 
                        String.format("%.2f", totalConsumption)));
            } else if (percentile <= 25.0) {
                suggestion.setSuggestionType("POSITIVE");
                suggestion.setMessage("Great job! This appliance is being used efficiently. Keep up the good work!");
            } else {
                continue;
            }
            
            suggestions.add(suggestion);
        }
        
        return suggestions;
    }
}
