package com.magis5.estoquedebebidas.domain.usecase.strategy.movements;

import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import org.springframework.stereotype.Component;

@Component
public interface MovimentoHistoricoStrategy {
    void registrar(Secao secao, Bebida bebida, TipoMovimento tipoMovimento, String responsavel, Double volume);
}
