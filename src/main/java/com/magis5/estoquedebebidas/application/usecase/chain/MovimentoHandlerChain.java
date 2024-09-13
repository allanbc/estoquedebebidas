package com.magis5.estoquedebebidas.application.usecase.chain;

import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.model.Bebida;
import com.magis5.estoquedebebidas.domain.model.Secao;

public interface MovimentoHandlerChain {
    void setNext(MovimentoHandlerChain handler);
    void handle(Secao secao, Bebida bebida, TipoMovimento tipoMovimento, String responsavel, Double volume);
}
