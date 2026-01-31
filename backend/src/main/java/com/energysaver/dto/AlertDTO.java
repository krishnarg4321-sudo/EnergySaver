package com.energysaver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertDTO {
    private Long id;
    private Long userId;
    private Long userApplianceId;
    private String applianceName;
    private String alertType;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
