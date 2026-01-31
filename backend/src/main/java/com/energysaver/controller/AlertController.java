package com.energysaver.controller;

import com.energysaver.dto.AlertResponseRequest;
import com.energysaver.entity.Alert;
import com.energysaver.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin
public class AlertController {

    @Autowired
    private AlertService alertService;

    @GetMapping("/")
    public ResponseEntity<List<Alert>> getUserAlerts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Alert> alerts = alertService.getUserAlerts(username);
        return ResponseEntity.ok(alerts);
    }

    @PutMapping("/{alertId}/read")
    public ResponseEntity<Alert> markAlertAsRead(@PathVariable Long alertId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Alert alert = alertService.markAlertAsRead(username, alertId);
        return ResponseEntity.ok(alert);
    }

    @PostMapping("/respond")
    public ResponseEntity<Alert> respondToAlert(@Valid @RequestBody AlertResponseRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Alert alert = alertService.respondToAlert(username, request);
        return ResponseEntity.ok(alert);
    }
}
