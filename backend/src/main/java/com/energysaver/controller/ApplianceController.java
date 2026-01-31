package com.energysaver.controller;

import com.energysaver.dto.AddApplianceRequest;
import com.energysaver.dto.ApplianceDTO;
import com.energysaver.dto.UserApplianceDTO;
import com.energysaver.service.ApplianceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appliances")
@CrossOrigin
public class ApplianceController {

    @Autowired
    private ApplianceService applianceService;

    @GetMapping("/")
    public ResponseEntity<List<ApplianceDTO>> getAllAppliances() {
        List<ApplianceDTO> appliances = applianceService.getAllAppliances();
        return ResponseEntity.ok(appliances);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserApplianceDTO>> getUserAppliances() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<UserApplianceDTO> userAppliances = applianceService.getUserAppliances(username);
        return ResponseEntity.ok(userAppliances);
    }

    @PostMapping("/user")
    public ResponseEntity<UserApplianceDTO> addUserAppliance(@Valid @RequestBody AddApplianceRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserApplianceDTO userAppliance = applianceService.addApplianceToUser(username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userAppliance);
    }

    @DeleteMapping("/user/{userApplianceId}")
    public ResponseEntity<Void> deleteUserAppliance(@PathVariable Long userApplianceId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        applianceService.deleteUserAppliance(username, userApplianceId);
        return ResponseEntity.noContent().build();
    }
}
