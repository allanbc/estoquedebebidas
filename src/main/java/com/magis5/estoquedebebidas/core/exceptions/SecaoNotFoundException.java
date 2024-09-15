package com.magis5.estoquedebebidas.core.exceptions;

import org.springframework.http.HttpStatus;

public class SecaoNotFoundException extends EstoqueBebidasException {
    public SecaoNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "secao_not_found", "Nenhuma seção encontrada com o id: " + id);
    }
}
