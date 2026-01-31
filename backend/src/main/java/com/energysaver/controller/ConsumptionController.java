package com.energysaver.controller;

import com.energysaver.dto.ConsumptionRequest;
import com.energysaver.entity.ConsumptionLog;
import com.energysaver.service.ConsumptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/consumption")
@CrossOrigin
public class ConsumptionController {

    @Autowired
    private ConsumptionService consumptionService;

    @PostMapping("/log")
    public ResponseEntity<ConsumptionLog> logConsumption(@Valid @RequestBody ConsumptionRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        ConsumptionLog consumptionLog = consumptionService.logConsumption(username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(consumptionLog);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ConsumptionLog>> getUserConsumption(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<ConsumptionLog> consumptionLogs = consumptionService.getUserConsumption(username, startDate, endDate);
        return ResponseEntity.ok(consumptionLogs);
    }
}
