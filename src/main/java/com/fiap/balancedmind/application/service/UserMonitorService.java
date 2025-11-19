package com.fiap.balancedmind.application.service;

import com.fiap.balancedmind.application.dto.monitor.GetUserMonitorResponseDTO;
import com.fiap.balancedmind.application.dto.monitor.MonitorRequestDTO;
import com.fiap.balancedmind.application.dto.monitor.MonitorResponseDTO;
import com.fiap.balancedmind.application.dto.user.UserProfileResponseDTO;
import com.fiap.balancedmind.domain.model.User;
import com.fiap.balancedmind.domain.model.UserMonitor;
import com.fiap.balancedmind.domain.repository.UserMonitorRepository;
import com.fiap.balancedmind.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMonitorService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMonitorRepository userMonitorRepository;

    @Autowired
    private final UserRepository userRepository;

    @Transactional
    public MonitorResponseDTO save(MonitorRequestDTO dto, Authentication auth) {
        String firebaseUid = auth.getName();

        UserProfileResponseDTO profile = userService.getByFirebaseUid(firebaseUid);

        UserMonitor monitor = userMonitorRepository.findById(profile.getUserId()).orElse(new UserMonitor());
        monitor.setUser(userRepository.findById(profile.getUserId()).orElse(new User()));
        monitor.setWorktime(dto.worktime());
        monitor.setRestQuality(dto.restQuality());
        monitor.setEmotionalState(dto.emotionalState());

        UserMonitor saved = userMonitorRepository.save(monitor);

        return new MonitorResponseDTO(
                saved.getUser().getUserId(),
                saved.getLastLog(),
                saved.getWorktime(),
                saved.getRestQuality(),
                saved.getEmotionalState()
        );
    }

    public GetUserMonitorResponseDTO get(Authentication auth) {
        String firebaseUid = auth.getName();
        UserProfileResponseDTO profile = userService.getByFirebaseUid(firebaseUid);

        List<UserMonitor> monitors = userMonitorRepository.findByUser_UserId(profile.getUserId());

        List<GetUserMonitorResponseDTO.MonitorDTO> dtoList = monitors.stream()
                .map(m -> new GetUserMonitorResponseDTO.MonitorDTO(
                        m.getUser().getUserId(),
                        m.getUser().getUsername(),
                        m.getLastLog(),
                        m.getWorktime(),
                        m.getRestQuality(),
                        m.getEmotionalState()
                ))
                .toList();

        return new GetUserMonitorResponseDTO(dtoList);
    }
}
