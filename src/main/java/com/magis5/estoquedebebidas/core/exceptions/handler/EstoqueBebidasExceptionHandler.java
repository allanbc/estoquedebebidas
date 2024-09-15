package com.magis5.estoquedebebidas.core.exceptions.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.magis5.estoquedebebidas.data.models.ErrorResponse;
import com.magis5.estoquedebebidas.core.exceptions.EstoqueBebidasException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.net.BindException;
import java.util.HashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Exception handler para erros que acontecem na camada do controller.
 */
@RestControllerAdvice
public class EstoqueBebidasExceptionHandler {

    private static final Logger LOG = getLogger(EstoqueBebidasExceptionHandler.class);

    private final HandleValidationRetrieveFields retrieveFields;

    private final HandleInvalidFormatRetrieveFields invalidFormatRetrieveFields;

    public EstoqueBebidasExceptionHandler(HandleValidationRetrieveFields retrieveFields, HandleInvalidFormatRetrieveFields invalidFormatRetrieveFields) {
        this.retrieveFields = retrieveFields;
        this.invalidFormatRetrieveFields = invalidFormatRetrieveFields;
    }

    @ExceptionHandler(EstoqueBebidasException.class)
    public ResponseEntity<ErrorResponse> handleEstoqueBebidasException(EstoqueBebidasException exception) {
        messageLog(exception);
        return ResponseEntity.status(exception.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .httpStatusCode(exception.getHttpStatus().value())
                        .errorCode(exception.getErrorCode())
                        .message(exception.getErrorDescription())
                        .fields(exception.getFields())
                        .build());
    }

    /**
     * Handler para erros de deserialização do jackson. Trata enums com valores errados.
     *
     * @param exception a exceção
     * @return response
     */
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException exception) {
        var fields = new HashMap<String, String>();

        String fieldsMessage = invalidFormatRetrieveFields.returnFiledsHandleInvalid(exception, fields);

        messageLog(exception);

        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .httpStatusCode(HttpStatus.BAD_REQUEST.value())
                        .errorCode("invalid_request")
                        .message(String.format("Campos inválidos: %s", fieldsMessage))
                        .fields(fields)
                        .build());
    }

    @ExceptionHandler({BindException.class, WebExchangeBindException.class, ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(Throwable exception) {
        var fields = new HashMap<String, String>();

        String fieldsMessage = retrieveFields.returnFieldsHandleValidation(exception, fields);

        messageLog(exception);

        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .httpStatusCode(HttpStatus.BAD_REQUEST.value())
                        .errorCode("invalid_request")
                        .message(String.format("Campos inválidos: %s", fieldsMessage))
                        .fields(fields)
                        .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        messageLog(exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errorCode("internal_server_error")
                        .message(exception.getMessage())
                        .build());
    }
    private <T> void messageLog (T exception) {
        if (LOG.isWarnEnabled()) {
            LOG.warn("Ocorreu a seguinte exceção: {}", exception.toString());
        }
    }

}

