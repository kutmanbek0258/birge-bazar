package kg.birge.bazar.paymentservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AuthorizedUser {
    private UUID id;
    private String username;
    private String name;
    private List<String> roles;
    private List<String> permissions;

    public static AuthorizedUser build(IntrospectionPrincipal principal) {
        if (principal == null) {
            return null;
        }
        AuthorizedUser user = new AuthorizedUser();
        user.setId(principal.getId());
        user.setUsername(principal.getUsername());
        user.setName(principal.getUsername());
        user.setRoles(principal.getRoles());
        user.setPermissions(principal.getPermissions());
        return user;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
