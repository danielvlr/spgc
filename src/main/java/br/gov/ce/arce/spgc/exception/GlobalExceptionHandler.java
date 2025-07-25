package br.gov.ce.arce.spgc.exception;

import br.gov.ce.arce.spgc.model.BaseResponse;
import br.gov.ce.arce.spgc.model.MessageResponseType;
import com.fasterxml.jackson.core.JsonParseException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    public static final String MSG_ERROR_MAX_UPLOAD_SIZE_EXCEED = "Arquivo ultrapassa o limite permitido";
    public static final String MSG_ERROR_ACCESS_DENIED = "Acesso Negado. Você não tem permissão para acessar esse recurso";

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<BaseResponse<Object>> handleAccessDeniedException(AccessDeniedException exception) {
//        log.error("Exception Handler [AccessDeniedException] - message::: {}", exception.getMessage());
//        return createBadRequest(MSG_ERROR_ACCESS_DENIED);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleException(Exception e) {
        log.error("Exception Handler [Exception] [{}]- message::: {}", e.getMessage(), e.getMessage());
        return createBadRequest(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse<Void>> handleBusinessException(BusinessException e) {
        log.error("Exception Handler [BusinessException] - message::: {}", e.getMessage());
        return this.createErrorResponse(e.getHttpStatus(), e.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<BaseResponse<Object>> handleMaxSizeException(MaxUploadSizeExceededException exception) {
        log.error("Exception Handler [MaxUploadSizeExceededException] - message::: {}", exception.getMessage());
        return createBadRequest(MSG_ERROR_MAX_UPLOAD_SIZE_EXCEED);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<BaseResponse<Object>> handleJsonParseException(JsonParseException exception) {
        log.error("Exception Handler [JsonParseException] - message::: {}", exception.getMessage());
        return createBadRequest(exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse<Object>> handleConstraintViolationException(ConstraintViolationException exception) {
        log.error("Exception Handler [ConstraintViolationException] - message::: {}", exception.getMessage());
        return createBadRequest(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        log.error("Exception Handler [MethodArgumentNotValidException] - message::: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        var statusCode = HttpStatus.BAD_REQUEST;
        BaseResponse<Object> response = BaseResponse.builder().messageType(MessageResponseType.ERROR)
                .result(errors).message(null).statusCode(statusCode.value()).build();

        return this.createResponseEntity(statusCode, response);
    }


    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse<Object>> handleTransactionSystemException(TransactionSystemException ex) {
        log.error("Exception Handler [TransactionSystemException] - message::: {}", ex.getMessage(), ex);

        Throwable rootCause = ex.getRootCause();
        Map<String, String> errors = new HashMap<>();
        String firstErrorMessage = "Erro ao salvar os dados";

        if (rootCause instanceof ConstraintViolationException violationEx) {
            firstErrorMessage = violationEx.getConstraintViolations().stream()
                    .map(violation -> violation.getMessage())
                    .findFirst()
                    .orElse("Erro de validação");

            violationEx.getConstraintViolations().forEach(v -> {
                String field = v.getPropertyPath().toString();
                errors.put(field, v.getMessage());
            });
        }

        HttpStatus statusCode = HttpStatus.BAD_REQUEST;

        BaseResponse<Object> response = BaseResponse.builder()
                .messageType(MessageResponseType.ERROR)
                .result(errors.isEmpty() ? null : errors)
                .message("Erro ao salvar os dados")
                .statusCode(statusCode.value())
                .build();

        return ResponseEntity.status(statusCode).body(response);
    }

    private ResponseEntity<BaseResponse<Void>> createErrorResponse(HttpStatus httpStatus, String message) {
        BaseResponse<Void> response = BaseResponse.<Void>builder()
                .statusCode(httpStatus.value())
                .message(message)
                .messageType(MessageResponseType.ERROR)
                .result(null)
                .build();

        return ResponseEntity.status(httpStatus).body(response);
    }

    private <T> ResponseEntity<BaseResponse<T>> createResponseEntity(HttpStatus httpStatus, BaseResponse<T> response) {
        return ResponseEntity.status(httpStatus).body(response);
    }

    private ResponseEntity<BaseResponse<Object>> createBadRequest(String message) {
        var statusCode = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(statusCode).body(BaseResponse.builder().messageType(MessageResponseType.ERROR)
                .message(message).statusCode(statusCode.value()).build());
    }
}
