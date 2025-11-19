package com.fiap.balancedmind.application.dto.monitor;

import java.time.*;

public record MonitorResponseDTO(
        Long userId,
        OffsetDateTime lastLog,
        Integer worktime,
        Integer restQuality,
        String emotionalState
) {
}
