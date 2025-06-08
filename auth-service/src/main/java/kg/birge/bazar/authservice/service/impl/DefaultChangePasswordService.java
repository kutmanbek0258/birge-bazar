package kg.birge.bazar.authservice.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kg.birge.bazar.authservice.components.ConfirmationStore;
import kg.birge.bazar.authservice.components.OTPStore;
import kg.birge.bazar.authservice.dto.security.AuthorizedUser;
import kg.birge.bazar.authservice.exception.ChangePasswordException;
import kg.birge.bazar.authservice.exception.InformationException;
import kg.birge.bazar.authservice.service.ChangePasswordService;
import kg.birge.bazar.authservice.service.MessageService;
import kg.birge.bazar.authservice.service.UserService;
import kg.birge.bazar.authservice.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DefaultChangePasswordService implements ChangePasswordService {

    private final static String PASSWORD_KEY = "password";

    private final OTPStore otpStore;
    private final ConfirmationStore changePasswordStore;
//    private final MailSenderService mailSenderService;
    private final MessageService messageService;
    private final UserService userService;

    @Override
    public void init(String newPassword, HttpServletResponse response) {
        AuthorizedUser authorizedUser = SecurityUtils.getAuthUser();
        OTPStore.GenerationResult generationResult = otpStore.generate(response);
        try {
            ConfirmationStore.StoreItem storeItem = new ConfirmationStore.StoreItem(
                authorizedUser.getEmail(),
                generationResult.otp(),
                Map.of(PASSWORD_KEY, newPassword)
            );
            changePasswordStore.save(storeItem, generationResult.sessionId());
        } catch (Exception e) {
            throw InformationException.builder("$happened.unexpected.error").build();
        }

//        mailSenderService.sendNewMail(
//            authorizedUser.getEmail(),
//            messageService.getMessage("email.subject.init.reset.password"),
//            ImmutableMap.<String, Object>builder()
//                .put("firstName", authorizedUser.getFirstName())
//                .put("otp", generationResult.otp())
//                .build()
//                .toString()
//        );
    }

    @Override
    public void confirmChange(String otp, HttpServletRequest request) {
        otp = otp.trim();
        if (!otpStore.validate(otp, request)) {
            throw new ChangePasswordException("$opt.incorrect");
        }

        String sessionId = otpStore.getSessionId(request);
        ConfirmationStore.StoreItem storeItem;
        try {
            storeItem = changePasswordStore.take(sessionId);
        } catch (Exception e) {
            throw InformationException.builder("$happened.unexpected.error").build();
        }

        Map<String, String> extraData = storeItem.extraData();
        if (extraData == null
            || !extraData.containsKey(PASSWORD_KEY)
            || StringUtils.isEmpty(extraData.get(PASSWORD_KEY))) {
            throw new ChangePasswordException("$data.not.found");
        }

        userService.changePassword(storeItem.email(), extraData.get(PASSWORD_KEY));
    }
}
