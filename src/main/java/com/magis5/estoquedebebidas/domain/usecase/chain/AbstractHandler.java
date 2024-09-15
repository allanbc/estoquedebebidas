package com.magis5.estoquedebebidas.domain.usecase.chain;

import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;

public abstract class AbstractHandler {
    protected AbstractHandler nextHandler;

    public void setNext(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void passToNext(Bebida bebida, double volume, String responsavel, TipoMovimento tipoMovimento) {
        if (nextHandler != null) {
            nextHandler.handle(bebida, volume, responsavel, tipoMovimento);
        }
    }

    public abstract void handle(Bebida bebidaDTO, double volume, String responsavel, TipoMovimento tipoMovimento);
}
