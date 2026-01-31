package com.energysaver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyStatsDTO {
    private Integer weekNumber;
    private Integer year;
    private Double totalWh;
    private Double weekOverWeekPercentage;
    private List<ChartDataPoint> pieChartData;
    private List<ChartDataPoint> barChartData;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartDataPoint {
        private String label;
        private Double value;
        private LocalDate date;
        
        public ChartDataPoint(String label, Double value) {
            this.label = label;
            this.value = value;
        }
    }
}
