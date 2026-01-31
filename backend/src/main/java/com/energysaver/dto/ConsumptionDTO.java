package com.energysaver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionDTO {
    private Long id;
    private Long userApplianceId;
    private String applianceName;
    private LocalDate logDate;
    private Double ratedWattsSnapshot;
    private Double hoursUsed;
    private Double whConsumed;
    private BigDecimal kwhConsumed;
}
