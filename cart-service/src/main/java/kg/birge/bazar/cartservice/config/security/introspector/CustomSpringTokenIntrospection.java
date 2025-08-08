package kg.birge.bazar.cartservice.config.security.introspector;

import cn.hutool.core.lang.Dict;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class CustomSpringTokenIntrospection implements OpaqueTokenIntrospector {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String introspectionUri;
    private final String clientId;
    private final String clientSecret;

    public CustomSpringTokenIntrospection(String introspectionUri, String clientId, String clientSecret,
                                          MappingJackson2HttpMessageConverter messageConverter) {
        this.introspectionUri = introspectionUri;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restTemplate.getMessageConverters().add(messageConverter);
    }

    @SneakyThrows
    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        String responseStr = restTemplate.postForObject(introspectionUri, request, String.class);

        Dict response = new ObjectMapper().readValue(responseStr, new TypeReference<>() {});
        TokenInfoDto tokenInfo = response.toBean(TokenInfoDto.class);

        return new CustomOAuth2AuthenticatedPrincipal(tokenInfo);
    }
}
