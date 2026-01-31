package com.energysaver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponseRequest {
    @NotNull(message = "Alert ID is required")
    private Long alertId;
    
    @NotNull(message = "User response is required")
    private String userResponse;
    
    private Double adjustmentPercentage;
}
