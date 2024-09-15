package com.magis5.estoquedebebidas.core.exceptions;

import org.springframework.http.HttpStatus;

public class MovimentoInvalidoException extends EstoqueBebidasException {
    public MovimentoInvalidoException(String message) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "movimento_invalido", String.format("Tipo de movimento inválido: %s", message));
    }
}
