package com.magis5.estoquedebebidas.core.exceptions;

import org.springframework.http.HttpStatus;

public class BebidaNotFoundException extends EstoqueBebidasException {
    public BebidaNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "bebida_not_found", String.format("Bebida não encontrada: %d", id));
    }
}
