package com.fiap.balancedmind.application.service;

import com.fiap.balancedmind.application.dto.ai.AiChatRequestDTO;
import com.fiap.balancedmind.application.dto.ai.AiChatResponseDTO;
import com.fiap.balancedmind.application.dto.ai.TheraApiResponseDTO;
import com.fiap.balancedmind.application.dto.user.UserProfileResponseDTO;
import com.fiap.balancedmind.infra.client.TheraAPIClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class AiChatService {

    @Autowired
    private TheraAPIClient theraAPIClient;

    @Autowired
    private UserService userService;

    public AiChatResponseDTO chat(AiChatRequestDTO request, Authentication authentication) {

        String firebaseUid = authentication.getName();

        log.info("Buscando perfil do usuário com firebaseUid: {}", firebaseUid);
        UserProfileResponseDTO profile = userService.getByFirebaseUid(firebaseUid);
        log.info("Perfil encontrado - username: {}", profile.getUsername());

        String enrichedMessage = String.format(
                "Olá, meu nome é %s. %s",
                profile.getUsername(),
                request.getMessage()
        );
        log.info("Mensagem enriquecida criada - moodRating: {}", request.getMoodRating());

        log.info("Chamando TheraAPIClient...");
        TheraApiResponseDTO theraResponse =
                theraAPIClient.callAiChat(enrichedMessage, request.getMoodRating());
        log.info("Resposta recebida do TheraAPIClient");

        String responseText = theraResponse.getResponse();

        List<String> techniques = Collections.emptyList();
        if (theraResponse.getMoodAnalysis() != null &&
                theraResponse.getMoodAnalysis().getSuggestedTechniques() != null) {
            techniques = theraResponse.getMoodAnalysis().getSuggestedTechniques();
        }

        return new AiChatResponseDTO(responseText, techniques);
    }
}
