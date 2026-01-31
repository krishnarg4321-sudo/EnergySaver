package com.energysaver.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionLogRequest {
    @NotNull(message = "User appliance ID is required")
    private Long userApplianceId;
    
    @NotNull(message = "Log date is required")
    private LocalDate logDate;
    
    @NotNull(message = "Hours used is required")
    @Min(value = 0, message = "Hours used must be at least 0")
    private Double hoursUsed;
}
