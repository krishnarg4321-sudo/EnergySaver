package com.energysaver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddApplianceRequest {
    @NotNull(message = "Appliance ID is required")
    private Long applianceId;
    
    private String customName;
    private Double ratedWatts;
}
