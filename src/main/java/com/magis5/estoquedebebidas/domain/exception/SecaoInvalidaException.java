package com.magis5.estoquedebebidas.domain.exception;

import org.springframework.http.HttpStatus;

public class SecaoInvalidaException extends EstoqueBebidasException {
    public SecaoInvalidaException(String message) {
        super(HttpStatus.BAD_REQUEST, "secao_invalida", String.format("secao inválida: %s", message));
    }
}
