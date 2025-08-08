package kg.birge.bazar.cartservice.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver.opaquetoken")
public class OAuth2ResourceOpaqueProperties {
    private String introspectionUri;
    private String clientId;
    private String clientSecret;
}
