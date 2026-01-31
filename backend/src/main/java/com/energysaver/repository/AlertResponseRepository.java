package com.energysaver.repository;

import com.energysaver.entity.AlertResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertResponseRepository extends JpaRepository<AlertResponse, Long> {
    List<AlertResponse> findByAlertId(Long alertId);
}
