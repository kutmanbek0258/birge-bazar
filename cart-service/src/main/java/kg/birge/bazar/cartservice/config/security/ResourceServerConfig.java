package kg.birge.bazar.cartservice.config.security;

import kg.birge.bazar.cartservice.config.security.introspector.CustomSpringTokenIntrospection;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor
public class ResourceServerConfig {

    private final OAuth2ResourceOpaqueProperties resourceProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(customizer -> customizer
                .requestMatchers("/v3/api-docs").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(configurer -> configurer
                .opaqueToken(customizer -> customizer
                    .introspector(new CustomSpringTokenIntrospection(
                        resourceProperties.getIntrospectionUri(),
                        resourceProperties.getClientId(),
                        resourceProperties.getClientSecret(),
                        new MappingJackson2HttpMessageConverter()
                    ))
                )
            );
        return http.build();
    }
}
