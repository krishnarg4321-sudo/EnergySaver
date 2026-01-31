package com.energysaver.controller;

import com.energysaver.dto.StatsResponse;
import com.energysaver.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/weekly")
    public ResponseEntity<StatsResponse> getWeeklyStats() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        StatsResponse stats = statsService.getWeeklyStats(username);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/monthly")
    public ResponseEntity<StatsResponse> getMonthlyStats() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        StatsResponse stats = statsService.getMonthlyStats(username);
        return ResponseEntity.ok(stats);
    }
}
