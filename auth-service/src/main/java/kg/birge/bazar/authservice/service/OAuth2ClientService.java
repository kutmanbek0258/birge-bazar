package kg.birge.bazar.authservice.service;

import kg.birge.bazar.authservice.dto.OAuth2ClientDto;
import kg.birge.bazar.authservice.dto.PageableResponseDto;

import java.util.Set;
import java.util.UUID;

public interface OAuth2ClientService {

    OAuth2ClientDto getByClientId(String clientId);

    PageableResponseDto<OAuth2ClientDto> searchClients(int page, int limit, String clientId, String clientName);

    OAuth2ClientDto save(OAuth2ClientDto dto);

    void delete(String clientId);

    String generateSecret(String clientId);

    /**
     * Уведомить всех клиентов об удалении пользователя(-ей), которые помечены в таблице user_clients как удалённые.
     */
    void notifyClientsAndClear();

    /**
     * Уведомить клиента с указанным clientId, что были удалены пользователи с указанными идентификаторами.
     */
    void notifyClientForDeleteAccount(String clientId, Set<UUID> userIds);
}
