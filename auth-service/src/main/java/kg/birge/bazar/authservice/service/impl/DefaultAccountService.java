package kg.birge.bazar.authservice.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.birge.bazar.authservice.dao.entity.UserEntity;
import kg.birge.bazar.authservice.dao.repository.UserRepository;
import kg.birge.bazar.authservice.dao.type.StoreType;
import kg.birge.bazar.authservice.dao.type.UserEventType;
import kg.birge.bazar.authservice.dto.FileStoreDto;
import kg.birge.bazar.authservice.dto.UserDto;
import kg.birge.bazar.authservice.dto.security.AuthorizedUser;
import kg.birge.bazar.authservice.exception.ServiceException;
import kg.birge.bazar.authservice.mapper.AuthorizedUserMapper;
import kg.birge.bazar.authservice.mapper.UserDtoMapper;
import kg.birge.bazar.authservice.service.*;
import kg.birge.bazar.authservice.service.security.SecurityService;
import kg.birge.bazar.authservice.utils.HttpUtils;
import kg.birge.bazar.authservice.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultAccountService implements AccountService {

    private final UserRepository userRepository;
    private final FileStoreService fileStoreService;
    private final SecurityService securityService;
    private final UserTokenService userTokenService;
    private final SecurityContextLogoutHandler securityContextLogoutHandler;
    private final UserClientService userClientService;
    private final UserEventService eventService;
    private final UserService userService;


    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser() {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        UserEntity entity = userRepository.getReferenceById(authorizedUser.getId());
        return UserDtoMapper.map(entity);
    }

    @Override
    @Transactional
    public UserDto save(
        UserDto dto,
        MultipartFile avatarFile,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        Optional<UserEntity> entityWrapper = userRepository.findById(dto.getId());
        if (entityWrapper.isEmpty()) {
            throw ServiceException.builder("Entity not found").build();
        }
        UserEntity entity = entityWrapper.get();

        if (avatarFile != null && !avatarFile.isEmpty()) {
            FileStoreDto fileStoreDto = fileStoreService.saveOrReplace(
                avatarFile,
                StoreType.AVATAR,
                entity.getAvatarFileId()
            );
            entity.setAvatarFileId(fileStoreDto != null ? fileStoreDto.getId() : null);
        }

        entity.setLastName(dto.getLastName());
        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setBirthday(dto.getBirthday());
        entity = userRepository.save(entity);

        AuthorizedUser updatedAuthorizedUser = AuthorizedUserMapper.reload(SecurityUtils.getAuthUser(), entity);
        securityService.reloadSecurityContext(updatedAuthorizedUser, request, response);
        return UserDtoMapper.map(entity);
    }

    @Override
    @Transactional
    public void deleteCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userTokenService.recallAllCurrentUserTokens();
        userClientService.markAsDelete(authorizedUser.getId());
        fileStoreService.delete(authorizedUser.getAvatarFileId());
        userRepository.deleteById(authorizedUser.getId());
        eventService.createEvent(UserEventType.USER_DELETE, null, request);
        securityContextLogoutHandler.logout(request, response, authentication);
    }

    @Override
    public ResponseEntity<byte[]> getAvatarCurrentUser() {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        try {
            UserService.UserAvatar userAvatar = userService.getUserAvatar(authorizedUser.getId());
            return HttpUtils.appendFileToResponse(
                userAvatar.storeDto().getId().toString(),
                userAvatar.storeDto().getContentType(),
                userAvatar.avatar()
            );
        } catch (ServiceException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
