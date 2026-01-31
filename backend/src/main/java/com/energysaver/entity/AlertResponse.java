package com.energysaver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "alert_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alert_id", nullable = false)
    private AlertNotification alert;
    
    @Column(name = "user_response", nullable = false)
    private String userResponse;
    
    @Column(name = "adjustment_percentage", precision = 5, scale = 2)
    private BigDecimal adjustmentPercentage;
    
    @Column(name = "responded_at", nullable = false, updatable = false)
    private LocalDateTime respondedAt;
    
    @PrePersist
    protected void onCreate() {
        respondedAt = LocalDateTime.now();
    }
}
