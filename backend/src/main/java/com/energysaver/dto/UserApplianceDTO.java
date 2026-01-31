package com.energysaver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserApplianceDTO {
    private Long id;
    private Long userId;
    private Long applianceId;
    private String customName;
    private Double ratedWatts;
    private String effectiveName;
    private Double effectiveRatedWatts;
    private String category;
    private LocalDateTime addedAt;
}
