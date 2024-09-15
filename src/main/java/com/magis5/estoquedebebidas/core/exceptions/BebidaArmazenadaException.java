package com.magis5.estoquedebebidas.core.exceptions;

import org.springframework.http.HttpStatus;

public class BebidaArmazenadaException extends EstoqueBebidasException {
    public BebidaArmazenadaException(String nomeBebida) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "bebida_armazenada_erro", String.format("Não é possível armazenar essa bebida, pois o volume total é maior que a capacidade máxima da seção: %s", nomeBebida));
    }
}
