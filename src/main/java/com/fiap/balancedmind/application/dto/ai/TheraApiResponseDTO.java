package com.fiap.balancedmind.application.dto.ai;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TheraApiResponseDTO {
    private String id;
    private String response;

    @JsonProperty("mood_analysis")
    private MoodAnalysis moodAnalysis;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime timestamp;

    @Getter
    @Setter
    public static class MoodAnalysis {
        @JsonProperty("current_rating")
        private Integer currentRating;

        @JsonProperty("suggested_techniques")
        private List<String> suggestedTechniques;

    }
}
