package kg.birge.bazar.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.birge.bazar.authservice.dto.UserDto;
import kg.birge.bazar.authservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Tag(name = "Контроллер управления профилем пользователя")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/current")
    @PreAuthorize("hasAnyAuthority('GET_OWN_DATA')")
    @Operation(description = "Получение информации о текущем пользователе")
    public UserDto getCurrentUser() {
        return accountService.getCurrentUser();
    }

    @PostMapping(
        value = "/current",
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE },
        produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    @PreAuthorize("hasAnyAuthority('CHANGE_OWN_DATA')")
    @Operation(description = "Сохранение изменений текущего пользователя")
    public UserDto saveUser(
        @RequestPart("dto") UserDto dto,
        @RequestPart(value = "file", required = false) MultipartFile avatarFile,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        return accountService.save(dto, avatarFile, request, response);
    }

    @DeleteMapping("/current")
    @PreAuthorize("hasAnyAuthority('DELETE_OWN_ACCOUNT')")
    @Operation(description = "Удаление текущего пользователя")
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        accountService.deleteCurrentUser(request, response);
    }

    @GetMapping(value = "/avatar/current")
    @PreAuthorize("hasAnyAuthority('GET_OWN_DATA')")
    @Operation(description = "Получение аватарки текущего пользователя")
    public ResponseEntity<byte[]> downloadAvatar() {
        return accountService.getAvatarCurrentUser();
    }
}
