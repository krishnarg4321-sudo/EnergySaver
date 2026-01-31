package com.energysaver.util;

/**
 * Single source of truth for all energy calculations.
 * All energy flows through: P → E = P × t → Wh → kWh
 */
public class EnergyCalculator {
    
    /**
     * Calculate energy consumption in Watt-hours (Wh)
     * Formula: E (Wh) = P (watts) × t (hours)
     * 
     * @param watts Power in watts
     * @param hours Time in hours
     * @return Energy in Watt-hours
     */
    public static double calculateWh(double watts, double hours) {
        if (watts < 0 || hours < 0) {
            throw new IllegalArgumentException("Watts and hours must be non-negative");
        }
        return watts * hours;
    }
    
    /**
     * Convert Watt-hours to kilowatt-hours
     * Formula: kWh = Wh ÷ 1000
     * 
     * @param wh Energy in Watt-hours
     * @return Energy in kilowatt-hours
     */
    public static double convertWhToKwh(double wh) {
        if (wh < 0) {
            throw new IllegalArgumentException("Watt-hours must be non-negative");
        }
        return wh / 1000.0;
    }
    
    /**
     * Calculate energy consumption directly in kilowatt-hours (kWh)
     * This is a convenience method that combines calculateWh and convertWhToKwh
     * 
     * @param watts Power in watts
     * @param hours Time in hours
     * @return Energy in kilowatt-hours
     */
    public static double calculateKwh(double watts, double hours) {
        double wh = calculateWh(watts, hours);
        return convertWhToKwh(wh);
    }
}
