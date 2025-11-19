package com.fiap.balancedmind.application.dto.monitor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MonitorRequestDTO(
        @NotNull @Min(0) @Max(12) Integer worktime,
        @NotNull @Min(1) @Max(5) Integer restQuality,
        @Size(max = 255) String emotionalState
) {
}
