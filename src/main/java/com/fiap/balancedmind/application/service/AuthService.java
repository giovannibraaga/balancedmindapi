package com.fiap.balancedmind.application.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiap.balancedmind.application.dto.user.UserProfileResponseDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final FirebaseAuth firebaseAuth;
    private final UserService userService;

    @Value("${firebase.apikey}")
    private String firebaseApiKey;

    public AuthService(FirebaseAuth firebaseAuth, UserService userService) {
        this.firebaseAuth = firebaseAuth;
        this.userService = userService;
    }

    private RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://identitytoolkit.googleapis.com")
                .build();
    }

    private RestClient secureTokenClient() {
        return RestClient.builder()
                .baseUrl("https://securetoken.googleapis.com")
                .build();
    }

    @Transactional
    public UserProfileResponseDTO signup(String email, String password, String username) {
        try {
            UserRecord.CreateRequest req = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(username)
                    .setEmailVerified(false)
                    .setDisabled(false);

            UserRecord rec = firebaseAuth.createUser(req);

            return userService.findOrCreate(rec.getUid(), email, username);

        } catch (FirebaseAuthException e) {
            log.warn("auth.signup.firebase.error email={} code={} msg={}",
                    email, e.getErrorCode(), e.getMessage());
            throw new IllegalArgumentException("Unable to create account");
        }
    }

    @Transactional
    public LoginResult login(String email, String password) {
        String path = "/v1/accounts:signInWithPassword?key=" + firebaseApiKey;
        SignInRequest payload = new SignInRequest(email, password, true);

        SignInResponse res;
        try {
            res = restClient().post()
                    .uri(path)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .body(SignInResponse.class);
        } catch (RestClientResponseException ex) {
            log.warn("auth.login.firebase.rest.error email={} status={} body={}",
                    email, ex.getRawStatusCode(), ex.getResponseBodyAsString());
            throw new IllegalArgumentException("Invalid credentials");
        } catch (Exception ex) {
            log.warn("auth.login.firebase.rest.error email={} msg={}", email, ex.getMessage());
            throw new IllegalArgumentException("Login failed");
        }

        if (res == null || res.idToken == null) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        final String uid;
        final String tokenEmail;
        final String tokenName;
        try {
            FirebaseToken decoded = firebaseAuth.verifyIdToken(res.idToken);
            uid = decoded.getUid();
            tokenEmail = decoded.getEmail() != null ? decoded.getEmail() : email;
            tokenName = decoded.getName();
        } catch (FirebaseAuthException e) {
            log.warn("auth.login.verify.error email={} msg={}", email, e.getMessage());
            throw new IllegalArgumentException("Invalid token returned by provider");
        }

        UserProfileResponseDTO user = userService.findOrCreate(
                uid,
                tokenEmail,
                tokenName != null ? tokenName : tokenEmail
        );

        return new LoginResult(user, res.idToken, res.refreshToken, res.expiresIn);
    }

    public RefreshResult refresh(String refreshToken) {
        log.info("auth.refresh.start refreshToken={}", refreshToken != null ? "present" : "null");
        String path = "/v1/token?key=" + firebaseApiKey;

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("refresh_token", refreshToken);

        log.info("auth.refresh.request path={}", path);

        RefreshResponse res;
        try {
            res = secureTokenClient().post()
                    .uri(path)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(RefreshResponse.class);

            log.info("auth.refresh.response received idToken={} refreshToken={} expiresIn={} userId={}",
                    res != null && res.idToken != null ? "present" : "null",
                    res != null && res.refreshToken != null ? "present" : "null",
                    res != null ? res.expiresIn : "null",
                    res != null ? res.userId : "null");
        } catch (RestClientResponseException ex) {
            log.warn("auth.refresh.firebase.rest.error status={} body={}",
                    ex.getRawStatusCode(), ex.getResponseBodyAsString());
            throw new IllegalArgumentException("Invalid refresh token");
        } catch (Exception ex) {
            log.warn("auth.refresh.firebase.rest.error msg={}", ex.getMessage());
            throw new IllegalArgumentException("Refresh failed");
        }

        if (res == null || res.idToken == null) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        try {
            FirebaseToken decoded = firebaseAuth.verifyIdToken(res.idToken());
            log.info("auth.refresh.verify uid={} email={}", decoded.getUid(), decoded.getEmail());
        } catch (FirebaseAuthException e) {
            log.warn("auth.refresh.verify.error msg={}", e.getMessage());
            throw new IllegalArgumentException("Invalid token returned by provider");
        }

        log.info("auth.refresh.success");

        return new RefreshResult(
                res.idToken(),
                res.refreshToken(),
                res.expiresIn()
        );
    }

    private record RefreshResponse(
            @JsonProperty("id_token") String idToken,
            @JsonProperty("refresh_token") String refreshToken,
            @JsonProperty("expires_in") String expiresIn,
            @JsonProperty("user_id") String userId
    ) {
    }

    public record RefreshResult(
            String idToken,
            String refreshToken,
            String expiresIn
    ) {
    }


    private record SignInRequest(
            String email,
            String password,
            @JsonProperty("returnSecureToken") boolean returnSecureToken
    ) {
    }

    private record SignInResponse(
            String idToken,
            String email,
            String refreshToken,
            String expiresIn,
            String localId
    ) {
    }

    public record LoginResult(
            UserProfileResponseDTO user,
            String idToken,
            String refreshToken,
            String expiresIn
    ) {
    }
}
