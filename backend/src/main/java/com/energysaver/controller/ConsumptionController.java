package com.energysaver.controller;

import com.energysaver.dto.ConsumptionLogRequest;
import com.energysaver.dto.ConsumptionDTO;
import com.energysaver.service.ConsumptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/consumption")
@CrossOrigin
public class ConsumptionController {

    @Autowired
    private ConsumptionService consumptionService;

    @PostMapping("/log")
    public ResponseEntity<ConsumptionDTO> logConsumption(@Valid @RequestBody ConsumptionLogRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        ConsumptionDTO consumptionDTO = consumptionService.logConsumption(username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(consumptionDTO);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ConsumptionDTO>> getUserConsumption(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<ConsumptionDTO> consumptionDTOs = consumptionService.getUserConsumption(username, startDate, endDate);
        return ResponseEntity.ok(consumptionDTOs);
    }
}
