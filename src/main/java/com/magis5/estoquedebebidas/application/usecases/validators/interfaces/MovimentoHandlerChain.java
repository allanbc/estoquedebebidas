package com.magis5.estoquedebebidas.application.usecases.validators.interfaces;

import com.magis5.estoquedebebidas.adapters.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;

public interface MovimentoHandlerChain {
    void setNext(MovimentoHandlerChain handler);
    void handle(Secao secao, Bebida bebida, MovimentoBebidasRequest request);
}
