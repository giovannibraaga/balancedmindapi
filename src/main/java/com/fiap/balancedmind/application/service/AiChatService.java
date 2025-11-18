package com.fiap.balancedmind.application.service;

import com.fiap.balancedmind.application.dto.ai.AiChatRequestDTO;
import com.fiap.balancedmind.application.dto.ai.AiChatResponseDTO;
import com.fiap.balancedmind.application.dto.ai.TheraApiResponseDTO;
import com.fiap.balancedmind.infra.client.TheraAPIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AiChatService {

    @Autowired
    private TheraAPIClient theraAPIClient;

    public AiChatResponseDTO chat (AiChatRequestDTO request) {
        TheraApiResponseDTO response = theraAPIClient.callAiChat(request.getMessage(), request.getMoodRating());

        String responseText = response.getResponse();

        List<String> techniques = Collections.emptyList();
        if (response.getMoodAnalysis() != null &&
                response.getMoodAnalysis().getSuggestedTechniques() != null) {
            techniques = response.getMoodAnalysis().getSuggestedTechniques();
        }

        return new AiChatResponseDTO(responseText, techniques);
    }
}
