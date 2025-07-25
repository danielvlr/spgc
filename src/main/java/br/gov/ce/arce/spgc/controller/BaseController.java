package br.gov.ce.arce.spgc.controller;

import br.gov.ce.arce.spgc.model.BaseResponse;
import br.gov.ce.arce.spgc.model.MessageResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.MessageFormat;

public class BaseController {
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    protected <T> ResponseEntity<BaseResponse<T>> okResponseEntity(T result) {
        return this.createResponseEntity(HttpStatus.OK, result, null, MessageResponseType.SUCCESS);
    }

    protected <T> ResponseEntity<BaseResponse<T>> okResponseEntity(T result, String message) {
        return this.createResponseEntity(HttpStatus.OK, result, message, MessageResponseType.SUCCESS);
    }

    protected ResponseEntity<BaseResponse<Void>> noContentResponse(String message) {
        return this.createResponseEntity(HttpStatus.NO_CONTENT, null, message, MessageResponseType.SUCCESS);
    }

    protected <T> ResponseEntity<BaseResponse<T>> createdResponseEntity(T result, String message) {
        return this.createResponseEntity(HttpStatus.CREATED, result, message, MessageResponseType.SUCCESS);
    }

    private <T> ResponseEntity<BaseResponse<T>> createResponseEntity(HttpStatus httpStatus, T result, String message, MessageResponseType type) {
        BaseResponse<T> response = BaseResponse.<T>builder()
                .statusCode(httpStatus.value())
                .message(message)
                .messageType(type)
                .result(result)
                .build();

        return ResponseEntity.status(httpStatus).body(response);
    }

    @SafeVarargs
    protected final <T> String formatMessage(String message, T... item) {
        return MessageFormat.format(message, item);
    }

}
