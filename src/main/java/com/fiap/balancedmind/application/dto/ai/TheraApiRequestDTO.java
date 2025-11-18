package com.fiap.balancedmind.application.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TheraApiRequestDTO {
    public String message;

    @JsonProperty("mood_rating")
    private Integer moodRating;

    public TheraApiRequestDTO(String message) {
        this.message = message;
    }
}
