package com.energysaver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_appliances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAppliance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appliance_id", nullable = false)
    private Appliance appliance;
    
    @Column(name = "custom_name")
    private String customName;
    
    @Column(name = "rated_watts")
    private Double ratedWatts;
    
    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;
    
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
    
    /**
     * Get the effective rated watts (custom or default)
     */
    public Double getEffectiveRatedWatts() {
        return ratedWatts != null ? ratedWatts : appliance.getDefaultRatedWatts();
    }
    
    /**
     * Get the effective name (custom or default)
     */
    public String getEffectiveName() {
        return customName != null && !customName.isEmpty() ? customName : appliance.getName();
    }
}
