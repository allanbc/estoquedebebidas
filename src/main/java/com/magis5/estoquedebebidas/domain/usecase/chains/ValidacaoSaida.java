package com.magis5.estoquedebebidas.domain.usecase.chains;

import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;

public interface ValidacaoSaida {
    boolean validar(Bebida bebidaDto, TipoBebida tipoBebida, Secao secao, double volume);
}
