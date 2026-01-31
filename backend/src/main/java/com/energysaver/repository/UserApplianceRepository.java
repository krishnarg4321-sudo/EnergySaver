package com.energysaver.repository;

import com.energysaver.entity.UserAppliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserApplianceRepository extends JpaRepository<UserAppliance, Long> {
    
    @Query("SELECT ua FROM UserAppliance ua JOIN FETCH ua.appliance WHERE ua.user.id = :userId")
    List<UserAppliance> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT ua FROM UserAppliance ua JOIN FETCH ua.appliance WHERE ua.id = :id AND ua.user.id = :userId")
    Optional<UserAppliance> findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    
    boolean existsByUserIdAndApplianceId(Long userId, Long applianceId);
}
