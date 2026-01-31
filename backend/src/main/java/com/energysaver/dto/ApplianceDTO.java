package com.energysaver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplianceDTO {
    private Long id;
    private String name;
    private String category;
    private Double defaultRatedWatts;
    private String description;
}
