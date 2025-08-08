package kg.birge.bazar.orderservice.config.security.introspector;

import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimAccessor;

import java.util.Map;

public abstract class TokenInfoOAuth2ClaimAccessor implements OAuth2TokenIntrospectionClaimAccessor {

    abstract TokenInfoDto getTokenInfo();

    @Override
    public Map<String, Object> getClaims() {
        return getTokenInfo().getClaims();
    }
}
