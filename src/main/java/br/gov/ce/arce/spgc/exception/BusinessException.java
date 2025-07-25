package br.gov.ce.arce.spgc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public static BusinessException createBadRequestBusinessException(String message) {
        return new BusinessException(message, HttpStatus.BAD_REQUEST);
    }

    public static BusinessException createConflictBusinessException(String message) {
        return new BusinessException(message, HttpStatus.CONFLICT);
    }

    public static BusinessException createNotFoundBusinessException(String message) {
        return new BusinessException(message, HttpStatus.NOT_FOUND);
    }

    public static BusinessException createUnauthorizedBusinessException(String message) {
        return new BusinessException(message, HttpStatus.UNAUTHORIZED);
    }

}
