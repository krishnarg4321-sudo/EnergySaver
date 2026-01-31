package com.energysaver.controller;

import com.energysaver.dto.UserApplianceRequest;
import com.energysaver.entity.Appliance;
import com.energysaver.entity.UserAppliance;
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
    public ResponseEntity<List<Appliance>> getAllAppliances() {
        List<Appliance> appliances = applianceService.getAllAppliances();
        return ResponseEntity.ok(appliances);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserAppliance>> getUserAppliances() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<UserAppliance> userAppliances = applianceService.getUserAppliances(username);
        return ResponseEntity.ok(userAppliances);
    }

    @PostMapping("/user")
    public ResponseEntity<UserAppliance> addUserAppliance(@Valid @RequestBody UserApplianceRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserAppliance userAppliance = applianceService.addUserAppliance(username, request);
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
