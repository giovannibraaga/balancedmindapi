package com.fiap.balancedmind.application.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiChatResponseDTO {
    private String response;
    private List<String> suggestedTechniques;

    public AiChatResponseDTO(String response) {
        this.response = response;
    }

}
