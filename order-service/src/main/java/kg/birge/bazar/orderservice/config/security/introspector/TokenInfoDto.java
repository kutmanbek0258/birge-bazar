package kg.birge.bazar.orderservice.config.security.introspector;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class TokenInfoDto {
    private boolean active;
    private String sub;
    private Object principal;
    @JsonProperty("client_id")
    private String clientId;
    private Long exp;
    private List<String> aud;
    private List<String> scope;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.scope.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getClaims() {
        return Map.of(
                "sub", sub,
                "principal", principal,
                "client_id", clientId,
                "exp", exp,
                "aud", aud,
                "scope", scope
        );
    }
}
