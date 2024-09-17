package com.magis5.estoquedebebidas.core.exceptions;

import org.springframework.http.HttpStatus;

public class AdicionarBebidaException extends EstoqueBebidasException {
    public AdicionarBebidaException(String s, String simpleName) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "bebida_adicionada_erro", String.format("Validação falhou no método %s: Não é possível adicionar a bebida %s na seção", simpleName, s));
    }
}
