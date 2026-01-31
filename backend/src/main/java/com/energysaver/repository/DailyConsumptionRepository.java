package com.energysaver.repository;

import com.energysaver.entity.DailyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface DailyConsumptionRepository extends JpaRepository<DailyConsumption, Long> {
    
    Optional<DailyConsumption> findByUserApplianceIdAndLogDate(Long userApplianceId, LocalDate logDate);
    
    // Weekly aggregation by user (Wh totals)
    @Query("SELECT SUM(dc.whConsumed) FROM DailyConsumption dc " +
           "WHERE dc.userAppliance.user.id = :userId " +
           "AND EXTRACT(YEAR FROM dc.logDate) = :year " +
           "AND EXTRACT(WEEK FROM dc.logDate) = :week")
    Double findWeeklyTotalWhByUser(@Param("userId") Long userId, 
                                     @Param("year") int year, 
                                     @Param("week") int week);
    
    // Monthly aggregation by user (kWh totals)
    @Query("SELECT SUM(dc.kwhConsumed) FROM DailyConsumption dc " +
           "WHERE dc.userAppliance.user.id = :userId " +
           "AND EXTRACT(YEAR FROM dc.logDate) = :year " +
           "AND EXTRACT(MONTH FROM dc.logDate) = :month")
    Double findMonthlyTotalKwhByUser(@Param("userId") Long userId, 
                                       @Param("year") int year, 
                                       @Param("month") int month);
    
    // Weekly consumption by appliance (for pie chart)
    @Query("SELECT ua.id, SUM(dc.whConsumed) FROM DailyConsumption dc " +
           "JOIN dc.userAppliance ua " +
           "WHERE ua.user.id = :userId " +
           "AND EXTRACT(YEAR FROM dc.logDate) = :year " +
           "AND EXTRACT(WEEK FROM dc.logDate) = :week " +
           "GROUP BY ua.id")
    List<Object[]> findWeeklyConsumptionByAppliance(@Param("userId") Long userId, 
                                                      @Param("year") int year, 
                                                      @Param("week") int week);
    
    // Daily totals for a week (for bar chart)
    @Query("SELECT dc.logDate, SUM(dc.whConsumed) FROM DailyConsumption dc " +
           "WHERE dc.userAppliance.user.id = :userId " +
           "AND dc.logDate BETWEEN :startDate AND :endDate " +
           "GROUP BY dc.logDate " +
           "ORDER BY dc.logDate")
    List<Object[]> findDailyTotalsForWeek(@Param("userId") Long userId, 
                                           @Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate);
    
    // Daily totals for a month (for line chart)
    @Query("SELECT dc.logDate, SUM(dc.kwhConsumed) FROM DailyConsumption dc " +
           "WHERE dc.userAppliance.user.id = :userId " +
           "AND EXTRACT(YEAR FROM dc.logDate) = :year " +
           "AND EXTRACT(MONTH FROM dc.logDate) = :month " +
           "GROUP BY dc.logDate " +
           "ORDER BY dc.logDate")
    List<Object[]> findDailyTotalsForMonth(@Param("userId") Long userId, 
                                            @Param("year") int year, 
                                            @Param("month") int month);
    
    // Top consumers for alert detection (75th percentile logic)
    @Query("SELECT dc FROM DailyConsumption dc " +
           "WHERE dc.userAppliance.user.id = :userId " +
           "AND dc.logDate = :date " +
           "ORDER BY dc.whConsumed DESC")
    List<DailyConsumption> findByUserIdAndDate(@Param("userId") Long userId, 
                                                 @Param("date") LocalDate date);
    
    // Total consumption by user appliance for ranking
    @Query("SELECT ua.id, SUM(dc.whConsumed) FROM DailyConsumption dc " +
           "JOIN dc.userAppliance ua " +
           "WHERE ua.user.id = :userId " +
           "GROUP BY ua.id " +
           "ORDER BY SUM(dc.whConsumed) DESC")
    List<Object[]> findTotalConsumptionByUserAppliance(@Param("userId") Long userId);
    
    // Find appliances with zero consumption
    @Query("SELECT ua FROM UserAppliance ua " +
           "WHERE ua.user.id = :userId " +
           "AND NOT EXISTS (SELECT 1 FROM DailyConsumption dc WHERE dc.userAppliance.id = ua.id)")
    List<Object> findUserAppliancesWithZeroConsumption(@Param("userId") Long userId);
}
