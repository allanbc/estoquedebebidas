package com.magis5.estoquedebebidas.core.exceptions;

import org.springframework.http.HttpStatus;

public class AdicionarBebidaException extends EstoqueBebidasException {
    public AdicionarBebidaException(String s) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "bebida_adicionada_erro", String.format("Não é possível adicionar a bebida %s na seção", s));
    }
}
