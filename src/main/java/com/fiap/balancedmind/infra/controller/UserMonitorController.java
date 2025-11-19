package com.fiap.balancedmind.infra.controller;

import com.fiap.balancedmind.application.dto.monitor.GetUserMonitorResponseDTO;
import com.fiap.balancedmind.application.dto.monitor.MonitorRequestDTO;
import com.fiap.balancedmind.application.dto.monitor.MonitorResponseDTO;
import com.fiap.balancedmind.application.service.UserMonitorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monitor")
public class UserMonitorController {

    @Autowired
    private UserMonitorService svc;

    public UserMonitorController(UserMonitorService svc) {
        this.svc = svc;
    }

    @PostMapping
    public ResponseEntity<MonitorResponseDTO> log(
            @Valid @RequestBody MonitorRequestDTO dto,
            Authentication authentication) {

        return ResponseEntity.ok(svc.save(dto, authentication));
    }

    @GetMapping
    public ResponseEntity<GetUserMonitorResponseDTO> get(Authentication authentication) {
        return ResponseEntity.ok(svc.get(authentication));
    }
}
