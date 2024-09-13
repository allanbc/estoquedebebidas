package com.magis5.estoquedebebidas.domain.exception;

import org.springframework.http.HttpStatus;

public class VolumeSecaoInvalida extends EstoqueBebidasException {
    public VolumeSecaoInvalida(Long id) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "volume_invalido", String.format("Volume de saída inválido ou seção não encontrada para o ID: %d", id));
    }
}
