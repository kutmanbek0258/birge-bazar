package kg.birge.bazar.authservice.exception;

import kg.birge.bazar.authservice.type.ErrorLevel;

public class RegistrationException extends InformationException {

    public RegistrationException(String description) {
        super(description, null, ErrorLevel.ERROR);
    }

    public RegistrationException(String message, Throwable cause) {
        super(message, cause, ErrorLevel.ERROR);
    }
}
