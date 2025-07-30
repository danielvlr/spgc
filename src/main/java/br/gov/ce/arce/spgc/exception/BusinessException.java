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

    public BusinessException(String message) {
        super(message);
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
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

    public static BusinessException createWithStatusBusinessException(String message, String statusCode) {
        try {
            HttpStatus status;

            // Tenta primeiro como código numérico
            if (statusCode.matches("\\d+")) {
                status = HttpStatus.valueOf(Integer.parseInt(statusCode));
            } else {
                status = HttpStatus.valueOf(statusCode.toUpperCase());
            }

            return new BusinessException(message, status);
        } catch (Exception e) {
            // fallback genérico se não for possível resolver o status
            return new BusinessException(message + " (status desconhecido: " + statusCode + ")", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
