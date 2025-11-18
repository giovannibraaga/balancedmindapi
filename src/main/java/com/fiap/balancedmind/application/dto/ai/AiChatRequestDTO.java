package com.fiap.balancedmind.application.dto.ai;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiChatRequestDTO {

    @NotBlank
    private String message;

    @Min(1)
    @Max(5)
    private Integer moodRating;

    public AiChatRequestDTO(String message) {
        this.message = message;
    }
}
