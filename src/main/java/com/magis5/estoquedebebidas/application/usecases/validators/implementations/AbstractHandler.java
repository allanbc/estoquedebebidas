package com.magis5.estoquedebebidas.application.usecases.validators.implementations;

import com.magis5.estoquedebebidas.adapters.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.entities.Bebida;

public abstract class AbstractHandler {
    protected AbstractHandler nextHandler;

    public void setNext(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void passToNext(Bebida bebida, MovimentoBebidasRequest request) {
        if (nextHandler != null) {
            nextHandler.handle(bebida, request);
        }
    }

    public abstract void handle(Bebida bebida, MovimentoBebidasRequest request);
}
