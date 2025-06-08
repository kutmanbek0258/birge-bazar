package kg.birge.bazar.authservice.mapper;

import kg.birge.bazar.authservice.dao.entity.SystemOauth2Client;
import kg.birge.bazar.authservice.dto.OAuth2ClientDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OAuth2ClientMapper {

    public OAuth2ClientDto map(SystemOauth2Client entity) {
        return OAuth2ClientDto.builder()
            .clientId(entity.getClientId())
            .clientSecret(entity.getClientSecret())
            .clientName(entity.getClientName())
            .clientSecretExpiresAt(
                entity.getClientSecretExpiresAt() != null ? entity.getClientSecretExpiresAt().toLocalDate() : null
            )
            .clientAuthenticationMethods(entity.getClientAuthenticationMethods())
            .authorizationGrantTypes(entity.getAuthorizationGrantTypes())
            .redirectUris(entity.getRedirectUris())
            .scopes(entity.getScopes())
            .deleteNotifyUris(entity.getDeleteNotifyUris())
            .build();
    }
}
