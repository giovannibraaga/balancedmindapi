package com.fiap.balancedmind.infra.controller;

import com.fiap.balancedmind.application.dto.user.UserProfileResponseDTO;
import com.fiap.balancedmind.application.service.AuthService;
import com.fiap.balancedmind.application.service.AuthService.LoginResult;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserProfileResponseDTO> signup(@RequestBody SignupRequest req) {
        UserProfileResponseDTO user = authService.signup(
                req.email(),
                req.password(),
                req.username()
        );
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        LoginResult result = authService.login(req.email(), req.password());
        return ResponseEntity.ok(
                new LoginResponse(
                        result.user(),
                        result.idToken(),
                        result.refreshToken(),
                        result.expiresIn()
                )
        );
    }

    public record SignupRequest(
            @NotBlank @Email String email,
            @NotBlank @Size(min = 6, max = 24) String password,
            @NotBlank @Size(max = 120) String username
    ) {}

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank @Size(min = 6, max = 24) String password
    ) {}

    public record LoginResponse(
            UserProfileResponseDTO user,
            String idToken,
            String refreshToken,
            String expiresIn
    ) {}
}
