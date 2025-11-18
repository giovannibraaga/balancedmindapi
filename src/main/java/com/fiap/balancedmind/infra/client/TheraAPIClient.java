package com.fiap.balancedmind.infra.client;

import com.fiap.balancedmind.application.dto.ai.TheraApiRequestDTO;
import com.fiap.balancedmind.application.dto.ai.TheraApiResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class TheraAPIClient {
    private final WebClient theraWebClient;

    public TheraApiResponseDTO callAiChat(String message, Integer moodRating) {
        TheraApiRequestDTO payload = new TheraApiRequestDTO(message, moodRating);

        return theraWebClient.post()
                .uri("/ai-chat")
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse
                                .bodyToMono(String.class)
                                .flatMap(body -> Mono.error(
                                        new RuntimeException("Erro ao chamar TheraAPI: " + body)
                                ))
                )
                .bodyToMono(TheraApiResponseDTO.class)
                .block();
    }
}
