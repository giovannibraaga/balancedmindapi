package com.fiap.balancedmind.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USER_MONITOR", schema = "ADMIN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserMonitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long monitorId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "last_log", insertable = false, updatable = false)
    private java.time.OffsetDateTime lastLog;

    private Integer worktime;
    private Integer restQuality;
    private String emotionalState;

    public UserMonitor(User user,
                       Integer worktime,
                       Integer restQuality,
                       String emotionalState) {
        this.user = user;
        this.worktime = worktime;
        this.restQuality = restQuality;
        this.emotionalState = emotionalState;
    }
}
