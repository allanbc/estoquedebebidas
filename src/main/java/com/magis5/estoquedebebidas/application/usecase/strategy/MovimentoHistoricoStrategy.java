package com.magis5.estoquedebebidas.application.usecase.strategy;

import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.model.Bebida;
import com.magis5.estoquedebebidas.domain.model.Secao;
import org.springframework.stereotype.Component;

@Component
public interface MovimentoHistoricoStrategy {
    void registrar(Secao secao, Bebida bebida, TipoMovimento tipoMovimento, String responsavel, Double volume);
}
