package kg.birge.bazar.authservice.dao.entity;

import jakarta.persistence.*;
import kg.birge.bazar.authservice.dao.entity.common.VersionedBusinessEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "sso", name = "users")
public class UserEntity extends VersionedBusinessEntity<UUID> {

    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "birthday")
    private LocalDate birthday;
    @Column(name = "avatar_file_id")
    private UUID avatarFileId;
    @Column(name = "active", nullable = false)
    private Boolean active;
    @Column(name = "admin", nullable = false)
    private Boolean admin;
    @Column(name = "superuser", nullable = false)
    private Boolean superuser;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(schema = "sso", name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    public List<RoleEntity> roles = new ArrayList<>();

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

}
