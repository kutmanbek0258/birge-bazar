package kg.birge.bazar.authservice.config.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.authorizationserver")
public class AuthorizationServerProperties {

    private String issuerUrl;
    private String introspectionEndpoint;
    private String authenticationSuccessUrl;
    private String savedRequestUrlStartsWith;
    private String customHandlerHeaderName;
    private String resetPasswordEndpoint;
    private long authorizationTtl;
    private long authorizationConsentTtl;
}
