package com.magis5.estoquedebebidas.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class EstoqueBebidasException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final Map<String, String> fields;

    public EstoqueBebidasException(HttpStatus httpStatus, String errorCode, String description) {
        this(httpStatus, errorCode, description, null);
    }

    public EstoqueBebidasException(HttpStatus httpStatus, String errorCode, String description, Map<String, String> fields) {
        super(description);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.fields = fields;
    }
    public String getErrorDescription() {
        return getMessage();
    }

    @Override
    public String toString() {
        return "EstoqueBebidasException{" +
                "httpStatus=" + httpStatus +
                ", errorCode='" + errorCode + '\'' +
                ", errorDescription='" + getErrorDescription() + '\'' +
                ", fields=" + fields +
                '}';
    }
}
