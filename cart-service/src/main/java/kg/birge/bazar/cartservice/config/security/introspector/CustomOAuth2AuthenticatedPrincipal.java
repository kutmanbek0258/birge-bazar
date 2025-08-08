package kg.birge.bazar.cartservice.config.security.introspector;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2AuthenticatedPrincipal extends TokenInfoOAuth2ClaimAccessor
    implements OAuth2AuthenticatedPrincipal, Serializable {

    private final TokenInfoDto tokenInfo;

    public CustomOAuth2AuthenticatedPrincipal(TokenInfoDto tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return tokenInfo.getAuthorities();
    }

    @Override
    public String getName() {
        return tokenInfo.getPrincipal().toString();
    }

    @Override
    TokenInfoDto getTokenInfo() {
        return this.tokenInfo;
    }
}
