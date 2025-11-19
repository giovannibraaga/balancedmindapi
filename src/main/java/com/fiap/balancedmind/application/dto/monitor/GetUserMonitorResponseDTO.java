package com.fiap.balancedmind.application.dto.monitor;

import java.time.OffsetDateTime;
import java.util.List;

public record GetUserMonitorResponseDTO(
        List<MonitorDTO> monitors
) {
    public record MonitorDTO(
            Long userId,
            String username,
            OffsetDateTime lastLog,
            Integer worktime,
            Integer restQuality,
            String emotionalState
    ) {
    }
}
