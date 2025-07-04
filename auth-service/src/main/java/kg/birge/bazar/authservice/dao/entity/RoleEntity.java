package kg.birge.bazar.authservice.dao.entity;

import jakarta.persistence.*;
import kg.birge.bazar.authservice.dao.entity.common.VersionedBusinessEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "sso", name = "roles")
public class RoleEntity extends VersionedBusinessEntity<String> {

    @Id
    @Column(name = "role_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "role_code", nullable = false)
    private String code;
    @Column(name = "role_description", nullable = false)
    private String description;
    @Column(name = "system_code", nullable = false)
    private String systemCode;
    @Column(name = "active")
    private Boolean active;

    @ManyToMany
    @JoinTable(schema = "sso", name = "role_authorities",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    public List<AuthorityEntity> authorities;

    @Override
    public String getId() {
        return this.code;
    }

    @Override
    public void setId(String code) {
        this.code = code;
    }

}
