package kg.birge.bazar.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.birge.bazar.authservice.dto.OAuth2ClientDto;
import kg.birge.bazar.authservice.dto.PageableResponseDto;
import kg.birge.bazar.authservice.service.OAuth2ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2-client")
@Tag(name = "Контроллер управления OAuth2 клиентами SSO")
public class OAuth2ClientController {

    private final OAuth2ClientService oAuth2ClientService;

    @GetMapping("/{clientId}")
    @PreAuthorize("hasAnyAuthority('GET_OAUTH_CLIENT_DATA')")
    @Operation(description = "Получение информации о клиенте по clientId")
    public OAuth2ClientDto getClientById(@PathVariable("clientId") String clientId) {
        return oAuth2ClientService.getByClientId(clientId);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('GET_OAUTH_CLIENT_DATA')")
    @Operation(description = "Поиск клиентов по параметрам")
    public PageableResponseDto<OAuth2ClientDto> searchClients(
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
        @RequestParam(value = "clientId", required = false) String clientId,
        @RequestParam(value = "clientName", required = false) String clientName
    ) {
        return oAuth2ClientService.searchClients(page, pageSize, clientId, clientName);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CHANGE_OAUTH_CLIENT_DATA')")
    @Operation(description = "Создание/изменение OAuth2 клиента")
    public OAuth2ClientDto save(@RequestBody OAuth2ClientDto dto) {
        return oAuth2ClientService.save(dto);
    }

    @PostMapping(value = "/gen-secret/{clientId}")
    @PreAuthorize("hasAnyAuthority('CHANGE_OAUTH_CLIENT_DATA')")
    @Operation(description = "Генерация и установка нового clientSecret для клиента с указанным clientId")
    public String generateSecret(@PathVariable String clientId) {
        return oAuth2ClientService.generateSecret(clientId);
    }

    @DeleteMapping(value = "/{clientId}")
    @PreAuthorize("hasAnyAuthority('DELETE_OAUTH_CLIENT_DATA')")
    @Operation(description = "Удалить OAuth2 клиента с указанным clientId")
    public void delete(@PathVariable("clientId") String clientId) {
        oAuth2ClientService.delete(clientId);
    }
}
