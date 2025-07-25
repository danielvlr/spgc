package br.gov.ce.arce.spgc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SecurityException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String messageCode;

    public SecurityException(String message, HttpStatus httpStatus, String messageCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.messageCode = messageCode;
    }

    public static SecurityException createJwtNotFoundSecurityException(String message, String messageCode) {
        return new SecurityException(message, HttpStatus.UNAUTHORIZED, messageCode);
    }

    public static SecurityException createUnauthorizedSecurityException(String message, String messageCode) {
        return new SecurityException(message, HttpStatus.UNAUTHORIZED, messageCode);
    }
}
