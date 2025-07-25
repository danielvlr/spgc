package br.gov.ce.arce.spgc.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

/**
 * Base response object
 *
 * @param statusCode  status code from the response
 * @param message     message from the response: success, warning or error message
 * @param messageType message type
 * @param result      any data information from the response
 * @param <T>         generic type object representing the data information
 * @author Eduarda
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record BaseResponse<T>(

        /**
         * Status code from response
         */
        int statusCode,

        /**
         * return message from the endpoint action
         */
        String message,
        /**
         * message type
         */
        MessageResponseType messageType,

        /**
         * Any result information (pagination object, list, object, file
         */
        T result
) {

    public static <T> BaseResponse<T> createSuccess(T result) {
        return createSuccess(null, result);
    }

    public static <T> BaseResponse<T> createSuccess(String message, T result) {
        return new BaseResponse<>(
                HttpStatus.CREATED.value(),
                message,
                MessageResponseType.SUCCESS,
                result
        );
    }

    /**
     * Response for OK status code with a success message.
     *
     * @param message message for success
     * @param result  data information
     * @param <T>     generic type
     * @return a response with data information
     * @author Eduarda
     */
    public static <T> BaseResponse<T> okSuccess(String message, T result) {
        return new BaseResponse<>(
                HttpStatus.OK.value(),
                message,
                MessageResponseType.SUCCESS,
                result
        );
    }

    /**
     * Response for OK status code with a success message.
     *
     * @param result  data information
     * @param <T>     generic type
     * @return a response with data information
     * @author Eduarda
     */
    public static <T> BaseResponse<T> okSuccess(T result) {
        return okSuccess(null, result);
    }
}
