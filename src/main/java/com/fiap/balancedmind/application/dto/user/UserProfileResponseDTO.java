package com.fiap.balancedmind.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDTO {

    private Long userId;
    private String firebaseUid;
    private String email;
    private String username;
    private LocalDateTime joinedAt;

    public UserProfileResponseDTO(Long userId, String firebaseUid,
                                  String email, String username) {
        this.userId = userId;
        this.firebaseUid = firebaseUid;
        this.email = email;
        this.username = username;
    }
}
