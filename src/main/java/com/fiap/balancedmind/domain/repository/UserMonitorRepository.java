package com.fiap.balancedmind.domain.repository;

import com.fiap.balancedmind.domain.model.UserMonitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMonitorRepository extends JpaRepository<UserMonitor, Long> {
    List<UserMonitor> findByUser_UserId(Long userId);
}
