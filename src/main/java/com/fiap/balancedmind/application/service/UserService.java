package com.fiap.balancedmind.application.service;

import com.fiap.balancedmind.application.dto.user.UserProfileResponseDTO;
import com.fiap.balancedmind.domain.model.User;
import com.fiap.balancedmind.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public UserProfileResponseDTO findOrCreate(String firebaseUid, String email, String username) {
        User existing = repo.findByFirebaseUid(firebaseUid).orElse(null);
        if (existing != null) {
            return toDto(existing);
        }

        User u = new User(
                firebaseUid,
                username != null ? username : (email != null ? email : firebaseUid),
                email,
                LocalDateTime.now()
        );
        User saved = repo.save(u);
        return toDto(saved);
    }

    public UserProfileResponseDTO getByFirebaseUid(String firebaseUid) {
        User u = repo.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new IllegalArgumentException("User not found for uid: " + firebaseUid));

        return toDto(u);
    }

    private UserProfileResponseDTO toDto(User u) {
        return new UserProfileResponseDTO(
                u.getUserId(),
                u.getFirebaseUid(),
                u.getEmail(),
                u.getUsername(),
                u.getJoinedAt()
        );
    }
}
