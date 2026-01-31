package com.energysaver.service;

import com.energysaver.dto.AddApplianceRequest;
import com.energysaver.dto.ApplianceDTO;
import com.energysaver.dto.UserApplianceDTO;
import com.energysaver.entity.Appliance;
import com.energysaver.entity.User;
import com.energysaver.entity.UserAppliance;
import com.energysaver.exception.ResourceNotFoundException;
import com.energysaver.repository.ApplianceRepository;
import com.energysaver.repository.UserApplianceRepository;
import com.energysaver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplianceService {
    
    private final ApplianceRepository applianceRepository;
    private final UserApplianceRepository userApplianceRepository;
    private final UserRepository userRepository;
    
    public ApplianceService(ApplianceRepository applianceRepository,
                           UserApplianceRepository userApplianceRepository,
                           UserRepository userRepository) {
        this.applianceRepository = applianceRepository;
        this.userApplianceRepository = userApplianceRepository;
        this.userRepository = userRepository;
    }
    
    @Transactional(readOnly = true)
    public List<ApplianceDTO> getAllAppliances() {
        return applianceRepository.findAll().stream()
                .map(this::convertToApplianceDTO)
                .collect(Collectors.toList());
    }
    
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }
    
    public UserApplianceDTO addApplianceToUser(String username, AddApplianceRequest request) {
        User user = getUserByUsername(username);
        
        Appliance appliance = applianceRepository.findById(request.getApplianceId())
                .orElseThrow(() -> new ResourceNotFoundException("Appliance not found with id: " + request.getApplianceId()));
        
        UserAppliance userAppliance = new UserAppliance();
        userAppliance.setUser(user);
        userAppliance.setAppliance(appliance);
        userAppliance.setCustomName(request.getCustomName());
        userAppliance.setRatedWatts(request.getRatedWatts());
        
        userAppliance = userApplianceRepository.save(userAppliance);
        
        return convertToUserApplianceDTO(userAppliance);
    }
    
    @Transactional(readOnly = true)
    public List<UserApplianceDTO> getUserAppliances(String username) {
        User user = getUserByUsername(username);
        
        return userApplianceRepository.findByUserId(user.getId()).stream()
                .map(this::convertToUserApplianceDTO)
                .collect(Collectors.toList());
    }
    
    public void deleteUserAppliance(String username, Long userApplianceId) {
        User user = getUserByUsername(username);
        UserAppliance userAppliance = userApplianceRepository.findByIdAndUserId(userApplianceId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User appliance not found with id: " + userApplianceId + " for user: " + username));
        
        userApplianceRepository.delete(userAppliance);
    }
    
    private ApplianceDTO convertToApplianceDTO(Appliance appliance) {
        ApplianceDTO dto = new ApplianceDTO();
        dto.setId(appliance.getId());
        dto.setName(appliance.getName());
        dto.setCategory(appliance.getCategory());
        dto.setDefaultRatedWatts(appliance.getDefaultRatedWatts());
        dto.setDescription(appliance.getDescription());
        return dto;
    }
    
    private UserApplianceDTO convertToUserApplianceDTO(UserAppliance userAppliance) {
        UserApplianceDTO dto = new UserApplianceDTO();
        dto.setId(userAppliance.getId());
        dto.setUserId(userAppliance.getUser().getId());
        dto.setApplianceId(userAppliance.getAppliance().getId());
        dto.setCustomName(userAppliance.getCustomName());
        dto.setRatedWatts(userAppliance.getRatedWatts());
        dto.setEffectiveName(userAppliance.getEffectiveName());
        dto.setEffectiveRatedWatts(userAppliance.getEffectiveRatedWatts());
        dto.setCategory(userAppliance.getAppliance().getCategory());
        dto.setAddedAt(userAppliance.getAddedAt());
        return dto;
    }
}
