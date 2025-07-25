package kg.birge.bazar.wmsservice.config.security.introspector;

import kg.birge.bazar.wmsservice.dto.AuthorizedUser;
import kg.birge.bazar.wmsservice.dto.TokenInfoDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.io.Serializable;
import java.util.*;

public class CustomOAuth2AuthenticatedPrincipal extends TokenInfoOAuth2ClaimAccessor
    implements OAuth2AuthenticatedPrincipal, Serializable {

    private static final String AUTHORITY_PREFIX = "SCOPE_";

    private final AuthorizedUser delegate;
    private final TokenInfoDto tokenInfo;

    public CustomOAuth2AuthenticatedPrincipal(TokenInfoDto tokenInfo) {
        this.delegate = AuthorizedUser.build(tokenInfo.getPrincipal());
        this.tokenInfo = tokenInfo;
    }

    public Map<String, Object> getAttributes() {
        return Collections.emptyMap();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (this.delegate != null) {
            authorities.addAll(delegate.getAuthorities());
        }
        if (this.tokenInfo != null && this.tokenInfo.getScopes() != null) {
            authorities.addAll(
                this.tokenInfo.getScopes()
                    .stream()
                    .map(item -> new SimpleGrantedAuthority(AUTHORITY_PREFIX + item))
                    .toList()
            );
        }
        return authorities;
    }

    /**
     * Если пришедший токен вне контекста пользователя (Client Credential Grant Type), то возвращаем client_id
     */
    public String getName() {
        if (this.delegate == null) {
            return this.tokenInfo.getClientId();
        }
        return this.delegate.getName();
    }

    @Override
    TokenInfoDto getTokenInfo() {
        return this.tokenInfo;
    }


}

