package com.magis5.estoquedebebidas.core.exceptions;

import org.springframework.http.HttpStatus;

public class SaidaEstoqueException extends EstoqueBebidasException {
    public SaidaEstoqueException(String nomeBebida) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "bebida_armazenada_erro",
                String.format("Não é possível retirar a bebida %s", nomeBebida));
    }
}
