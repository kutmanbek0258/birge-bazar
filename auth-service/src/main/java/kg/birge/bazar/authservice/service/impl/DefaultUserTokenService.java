package kg.birge.bazar.authservice.service.impl;

import kg.birge.bazar.authservice.dao.entity.SystemOauth2Client;
import kg.birge.bazar.authservice.dao.repository.SystemOAuth2ClientRepository;
import kg.birge.bazar.authservice.dto.UserTokenInfoDto;
import kg.birge.bazar.authservice.dto.security.AuthorizationInfo;
import kg.birge.bazar.authservice.dto.security.AuthorizedUser;
import kg.birge.bazar.authservice.service.ReferenceService;
import kg.birge.bazar.authservice.service.UserTokenService;
import kg.birge.bazar.authservice.service.security.RedisOAuth2AuthorizationService;
import kg.birge.bazar.authservice.utils.SecurityUtils;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class DefaultUserTokenService implements UserTokenService {

    private final RedisOAuth2AuthorizationService authorizationService;
    private final SystemOAuth2ClientRepository systemOauth2ClientRepository;
    private final ReferenceService referenceService;

    public DefaultUserTokenService(
        OAuth2AuthorizationService authorizationService,
        SystemOAuth2ClientRepository systemOauth2ClientRepository,
        ReferenceService referenceService
    ) {
        this.authorizationService = (RedisOAuth2AuthorizationService) authorizationService;
        this.systemOauth2ClientRepository = systemOauth2ClientRepository;
        this.referenceService = referenceService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserTokenInfoDto> getUserTokens() {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        List<AuthorizationInfo> userTokens = authorizationService.findInfoByUserId(authorizedUser.getId());
        return userTokens.stream()
            .map(this::mapToDto)
            .sorted(Comparator.comparing(UserTokenInfoDto::getLastRefreshDate).reversed())
            .toList();
    }

    public UserTokenInfoDto mapToDto(AuthorizationInfo entity) {
        SystemOauth2Client client = systemOauth2ClientRepository.getByClientId(entity.getClientId());
        AuthorizationGrantType grantType = entity.getAuthorizationGrantType();

        return UserTokenInfoDto.builder()
            .authorizationId(entity.getAuthorizationId())
            .grantTypeName(referenceService.getGrantTypeName(grantType))
            .lastRefreshDate(entity.getLastRefreshDate())
            .startDate(entity.getStartDate())
            .scopeNames(referenceService.getScopeNames(entity.getScopes()))
            .clientName(client.getClientName())
            .clientId(entity.getClientId())
            .clientRedirectUri(entity.getRedirectUri())
            .build();
    }

    @Override
    public void recallToken(String authenticationId) {
        OAuth2Authorization authorization = authorizationService.findById(authenticationId);
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        if (authorization != null && authorization.getPrincipalName().equals(authorizedUser.getUsername())) {
            authorizationService.remove(authenticationId);
        }
    }

    @Override
    public void recallAllCurrentUserTokens() {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        List<AuthorizationInfo> userTokens = authorizationService.findInfoByUserId(authorizedUser.getId());
        for (AuthorizationInfo authInfo : userTokens) {
            authorizationService.remove(authInfo.getAuthorizationId());
        }
    }
}
