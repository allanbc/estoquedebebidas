package com.magis5.estoquedebebidas.core.exceptions;

import org.springframework.http.HttpStatus;

public class VolumeSecaoInvalidaException extends EstoqueBebidasException {
    public VolumeSecaoInvalidaException(Long id) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "volume_invalido", String.format("Volume inválido ou seção não encontrada para o ID: %d", id));
    }
}
