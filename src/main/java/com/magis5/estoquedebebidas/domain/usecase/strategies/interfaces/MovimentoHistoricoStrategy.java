package com.magis5.estoquedebebidas.domain.usecase.strategies.interfaces;

import com.magis5.estoquedebebidas.data.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import org.springframework.stereotype.Component;

@Component
public interface MovimentoHistoricoStrategy {
    void registrar(Secao secao, Bebida bebida, MovimentoBebidasRequest request);
}
