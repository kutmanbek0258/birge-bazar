package kg.birge.bazar.authservice.service;

import kg.birge.bazar.authservice.dto.UserTokenInfoDto;

import java.util.List;

public interface UserTokenService {

    List<UserTokenInfoDto> getUserTokens();

    void recallToken(String authenticationId);

    void recallAllCurrentUserTokens();
}
