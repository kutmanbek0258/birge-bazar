package kg.birge.bazar.authservice.exception;


import kg.birge.bazar.authservice.type.ErrorLevel;

public class ChangePasswordException extends InformationException {

    public ChangePasswordException(String description) {
        super(description, null, ErrorLevel.ERROR);
    }

    public ChangePasswordException(String message, Throwable cause) {
        super(message, cause, ErrorLevel.ERROR);
    }
}
