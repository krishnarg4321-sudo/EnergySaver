package com.energysaver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "appliances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appliance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String category;
    
    @Column(name = "default_rated_watts", nullable = false)
    private Double defaultRatedWatts;
    
    private String description;
}
