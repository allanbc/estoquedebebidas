package com.magis5.estoquedebebidas.application.usecases.strategies.interfaces;

import com.magis5.estoquedebebidas.adapters.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import org.springframework.stereotype.Component;

@Component
public interface MovimentoHistoricoStrategy {
    void registrar(Secao secao, Bebida bebida, MovimentoBebidasRequest request);
}
