package com.magis5.estoquedebebidas.domain.usecase.chain;

import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;

public interface MovimentoHandlerChain {
    void setNext(MovimentoHandlerChain handler);
    void handle(Secao secao, Bebida bebida, TipoMovimento tipoMovimento, String responsavel, Double volume);
}
