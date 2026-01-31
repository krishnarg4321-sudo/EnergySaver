package com.energysaver.controller;

import com.energysaver.dto.WeeklyStatsDTO;
import com.energysaver.dto.MonthlyStatsDTO;
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
    public ResponseEntity<WeeklyStatsDTO> getWeeklyStats() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        WeeklyStatsDTO stats = statsService.getWeeklyStats(username);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyStatsDTO> getMonthlyStats() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        MonthlyStatsDTO stats = statsService.getMonthlyStats(username);
        return ResponseEntity.ok(stats);
    }
}
