package com.energysaver.service;

import com.energysaver.dto.MonthlyStatsDTO;
import com.energysaver.dto.WeeklyStatsDTO;
import com.energysaver.entity.UserAppliance;
import com.energysaver.repository.DailyConsumptionRepository;
import com.energysaver.repository.UserApplianceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class StatsService {
    
    private final DailyConsumptionRepository dailyConsumptionRepository;
    private final UserApplianceRepository userApplianceRepository;
    
    public StatsService(DailyConsumptionRepository dailyConsumptionRepository,
                       UserApplianceRepository userApplianceRepository) {
        this.dailyConsumptionRepository = dailyConsumptionRepository;
        this.userApplianceRepository = userApplianceRepository;
    }
    
    public WeeklyStatsDTO getWeeklyStats(Long userId) {
        LocalDate today = LocalDate.now();
        int currentWeek = today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int currentYear = today.get(IsoFields.WEEK_BASED_YEAR);
        
        LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        
        Double totalWh = dailyConsumptionRepository.findWeeklyTotalWhByUser(userId, currentYear, currentWeek);
        totalWh = totalWh != null ? totalWh : 0.0;
        
        List<Object[]> applianceConsumption = dailyConsumptionRepository
                .findWeeklyConsumptionByAppliance(userId, currentYear, currentWeek);
        
        List<WeeklyStatsDTO.ChartDataPoint> pieChartData = new ArrayList<>();
        Map<Long, String> applianceNames = new HashMap<>();
        
        for (UserAppliance ua : userApplianceRepository.findByUserId(userId)) {
            applianceNames.put(ua.getId(), ua.getEffectiveName());
        }
        
        for (Object[] row : applianceConsumption) {
            Long applianceId = ((Number) row[0]).longValue();
            Double consumption = ((Number) row[1]).doubleValue();
            String name = applianceNames.getOrDefault(applianceId, "Unknown");
            pieChartData.add(new WeeklyStatsDTO.ChartDataPoint(name, consumption));
        }
        
        List<Object[]> dailyTotals = dailyConsumptionRepository
                .findDailyTotalsForWeek(userId, weekStart, weekEnd);
        
        List<WeeklyStatsDTO.ChartDataPoint> barChartData = new ArrayList<>();
        for (Object[] row : dailyTotals) {
            LocalDate date = (LocalDate) row[0];
            Double consumption = ((Number) row[1]).doubleValue();
            barChartData.add(new WeeklyStatsDTO.ChartDataPoint(
                    date.getDayOfWeek().toString(), consumption, date));
        }
        
        LocalDate previousWeekDate = today.minusWeeks(1);
        int previousWeek = previousWeekDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int previousYear = previousWeekDate.get(IsoFields.WEEK_BASED_YEAR);
        
        Double previousWeekTotal = dailyConsumptionRepository
                .findWeeklyTotalWhByUser(userId, previousYear, previousWeek);
        previousWeekTotal = previousWeekTotal != null ? previousWeekTotal : 0.0;
        
        Double weekOverWeekPercentage = calculatePercentageChange(previousWeekTotal, totalWh);
        
        WeeklyStatsDTO dto = new WeeklyStatsDTO();
        dto.setWeekNumber(currentWeek);
        dto.setYear(currentYear);
        dto.setTotalWh(totalWh);
        dto.setWeekOverWeekPercentage(weekOverWeekPercentage);
        dto.setPieChartData(pieChartData);
        dto.setBarChartData(barChartData);
        
        return dto;
    }
    
    public MonthlyStatsDTO getMonthlyStats(Long userId) {
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();
        
        Double totalKwhDouble = dailyConsumptionRepository
                .findMonthlyTotalKwhByUser(userId, currentYear, currentMonth);
        totalKwhDouble = totalKwhDouble != null ? totalKwhDouble : 0.0;
        BigDecimal totalKwh = BigDecimal.valueOf(totalKwhDouble);
        
        List<Object[]> dailyTotals = dailyConsumptionRepository
                .findDailyTotalsForMonth(userId, currentYear, currentMonth);
        
        List<MonthlyStatsDTO.ChartDataPoint> lineChartData = new ArrayList<>();
        for (Object[] row : dailyTotals) {
            LocalDate date = (LocalDate) row[0];
            Double consumption = ((Number) row[1]).doubleValue();
            lineChartData.add(new MonthlyStatsDTO.ChartDataPoint(
                    String.valueOf(date.getDayOfMonth()), consumption, date));
        }
        
        LocalDate previousMonthDate = today.minusMonths(1);
        int previousMonth = previousMonthDate.getMonthValue();
        int previousYear = previousMonthDate.getYear();
        
        Double currentMonthTotal = dailyConsumptionRepository
                .findMonthlyTotalKwhByUser(userId, currentYear, currentMonth);
        currentMonthTotal = currentMonthTotal != null ? currentMonthTotal : 0.0;
        
        Double previousMonthTotal = dailyConsumptionRepository
                .findMonthlyTotalKwhByUser(userId, previousYear, previousMonth);
        previousMonthTotal = previousMonthTotal != null ? previousMonthTotal : 0.0;
        
        List<MonthlyStatsDTO.ChartDataPoint> barChartData = new ArrayList<>();
        barChartData.add(new MonthlyStatsDTO.ChartDataPoint("Previous Month", previousMonthTotal));
        barChartData.add(new MonthlyStatsDTO.ChartDataPoint("Current Month", currentMonthTotal));
        
        Double monthOverMonthPercentage = calculatePercentageChange(previousMonthTotal, currentMonthTotal);
        
        MonthlyStatsDTO dto = new MonthlyStatsDTO();
        dto.setMonth(currentMonth);
        dto.setYear(currentYear);
        dto.setTotalKwh(totalKwh);
        dto.setMonthOverMonthPercentage(monthOverMonthPercentage);
        dto.setLineChartData(lineChartData);
        dto.setBarChartData(barChartData);
        
        return dto;
    }
    
    private Double calculatePercentageChange(Double oldValue, Double newValue) {
        if (oldValue == null || oldValue == 0.0) {
            return newValue > 0 ? 100.0 : 0.0;
        }
        return ((newValue - oldValue) / oldValue) * 100.0;
    }
}
