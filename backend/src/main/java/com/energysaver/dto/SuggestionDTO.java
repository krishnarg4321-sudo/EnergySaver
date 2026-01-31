package com.energysaver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionDTO {
    private Long userApplianceId;
    private String applianceName;
    private Double totalConsumption;
    private Double percentile;
    private String suggestionType;
    private String message;
}
