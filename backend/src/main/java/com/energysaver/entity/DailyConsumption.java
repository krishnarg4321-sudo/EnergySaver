package com.energysaver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_consumption", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_appliance_id", "log_date"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyConsumption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_appliance_id", nullable = false)
    private UserAppliance userAppliance;
    
    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;
    
    @Column(name = "rated_watts_snapshot", nullable = false)
    private Double ratedWattsSnapshot;
    
    @Column(name = "hours_used", nullable = false, precision = 10, scale = 2)
    private BigDecimal hoursUsed;
    
    @Column(name = "wh_consumed", nullable = false, precision = 10, scale = 2)
    private BigDecimal whConsumed;
    
    @Column(name = "kwh_consumed", nullable = false, precision = 10, scale = 6)
    private BigDecimal kwhConsumed;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
