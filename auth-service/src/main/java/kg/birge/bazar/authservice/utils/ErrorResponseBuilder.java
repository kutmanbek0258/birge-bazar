package kg.birge.bazar.authservice.utils;

import kg.birge.bazar.authservice.dto.ErrorResponseDto;
import kg.birge.bazar.authservice.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class ErrorResponseBuilder {

    private final MessageService messageService;

    public ResponseEntity<ErrorResponseDto> makeResponse(String messageCode, Throwable cause) {
        return this.makeResponse(messageCode, HttpStatus.INTERNAL_SERVER_ERROR, cause, false);
    }

    public ResponseEntity<ErrorResponseDto> makeResponse(
        String messageCode,
        HttpStatus status,
        Throwable cause,
        boolean withoutStacktrace
    ) {
        String message = messageService.getMessage(messageCode, cause.getMessage());
        ErrorResponseDto dto;
        if (withoutStacktrace) {
            dto = ErrorResponseDto.builder(message).build();
        } else {
            dto = ErrorResponseDto.builder(message, cause).build();
        }
        return new ResponseEntity<>(dto, status);
    }

    public ResponseEntity<ErrorResponseDto> makeResponse(Throwable cause) {
        return this.makeResponse("common.exception", HttpStatus.INTERNAL_SERVER_ERROR, cause, false);
    }

    public ResponseEntity<ErrorResponseDto> makeResponseWithoutStacktrace(Throwable cause) {
        return this.makeResponse("common.exception", HttpStatus.INTERNAL_SERVER_ERROR, cause, true);
    }
}
