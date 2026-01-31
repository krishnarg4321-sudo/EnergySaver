package com.energysaver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatsDTO {
    private Integer month;
    private Integer year;
    private BigDecimal totalKwh;
    private Double monthOverMonthPercentage;
    private List<ChartDataPoint> lineChartData;
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
