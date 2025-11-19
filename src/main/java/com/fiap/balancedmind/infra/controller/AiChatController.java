package com.fiap.balancedmind.infra.controller;

import com.fiap.balancedmind.application.dto.ai.AiChatRequestDTO;
import com.fiap.balancedmind.application.dto.ai.AiChatResponseDTO;
import com.fiap.balancedmind.application.service.AiChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/ai")
@Tag(name = "AI Chat", description = "Gateway para TheraAPI /ai-chat")
@Slf4j
public class AiChatController {
    @Autowired
    private AiChatService aiChatService;

    @PostMapping("/chat")
    public ResponseEntity<AiChatResponseDTO> chat(
            @Valid @RequestBody AiChatRequestDTO requestDTO,
            Authentication authentication) {
        try {
            AiChatResponseDTO response = aiChatService.chat(requestDTO, authentication);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            log.warn("Validation Error {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AiChatResponseDTO("Invalid Data: " + ex.getMessage()));
        } catch (Exception ex) {
            log.error("Unexpected error while processing ", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AiChatResponseDTO("Internal error while processing the request."));
        }
    }
}
