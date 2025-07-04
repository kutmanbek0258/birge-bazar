package kg.birge.bazar.authservice.dao.entity;

import jakarta.persistence.*;
import kg.birge.bazar.authservice.dao.type.UserEventType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "sso", name = "user_events")
public class UserEventEntity {

    @Id
    @Column(name = "event_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private UserEventType eventType;

    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "agent_browser")
    private String browser;

    @Column(name = "agent_device")
    private String device;

    @Column(name = "agent_os")
    private String os;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime creationDate;
}
