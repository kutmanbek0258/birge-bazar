package kg.birge.bazar.authservice.service;

import kg.birge.bazar.authservice.dao.entity.UserEntity;
import kg.birge.bazar.authservice.dto.security.AuthorizedUser;
import kg.birge.bazar.authservice.type.AuthProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AuthProviderService {

    /**
     * Создание или обновление пользователя используя сервис-провайдер
     */
    UserEntity save(OAuth2User userDto, AuthProvider provider);

    /**
     * Создание или обновление пользователя с последующим маппингом в сущность AuthorizedUser
     */
    AuthorizedUser saveAndMap(OAuth2User userDto, AuthProvider provider);
}
