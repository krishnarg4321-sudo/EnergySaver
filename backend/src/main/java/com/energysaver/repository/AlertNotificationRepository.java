package com.energysaver.repository;

import com.energysaver.entity.AlertNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertNotificationRepository extends JpaRepository<AlertNotification, Long> {
    
    @Query("SELECT a FROM AlertNotification a WHERE a.user.id = :userId ORDER BY a.createdAt DESC")
    List<AlertNotification> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT a FROM AlertNotification a WHERE a.user.id = :userId AND a.isRead = false ORDER BY a.createdAt DESC")
    List<AlertNotification> findUnreadByUserId(@Param("userId") Long userId);
    
    long countByUserIdAndIsRead(Long userId, Boolean isRead);
}
