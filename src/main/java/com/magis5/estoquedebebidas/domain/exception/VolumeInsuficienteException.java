package com.magis5.estoquedebebidas.domain.exception;

import org.springframework.http.HttpStatus;

public class VolumeInsuficienteException extends EstoqueBebidasException {
    public VolumeInsuficienteException(Long id) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "volume_insuficiente", String.format("Volume insuficiente ou regra de remoção inválida para o ID: %d", id));
    }


}
