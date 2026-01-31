package com.energysaver.controller;

import com.energysaver.dto.AlertResponseRequest;
import com.energysaver.dto.AlertDTO;
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
    public ResponseEntity<List<AlertDTO>> getUserAlerts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<AlertDTO> alerts = alertService.getUserAlerts(username);
        return ResponseEntity.ok(alerts);
    }

    @PutMapping("/{alertId}/read")
    public ResponseEntity<AlertDTO> markAlertAsRead(@PathVariable Long alertId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        AlertDTO alert = alertService.markAlertAsRead(alertId, username);
        return ResponseEntity.ok(alert);
    }

    @PostMapping("/respond")
    public ResponseEntity<AlertDTO> respondToAlert(@Valid @RequestBody AlertResponseRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        alertService.respondToAlert(request, username);
        return ResponseEntity.ok().build();
    }
}
