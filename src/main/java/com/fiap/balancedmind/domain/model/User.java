package com.fiap.balancedmind.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS", schema = "ADMIN")
public class User {

    @Column(name = "USER_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "FIREBASE_UID", nullable = false, unique = true)
    private String firebaseUid;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "JOINED_AT", nullable = false)
    private LocalDateTime joinedAt;

    public User(String firebaseUid, String username, String email, LocalDateTime joinedAt) {
        this.firebaseUid = firebaseUid;
        this.username = username;
        this.email = email;
        this.joinedAt = joinedAt;
    }
}
