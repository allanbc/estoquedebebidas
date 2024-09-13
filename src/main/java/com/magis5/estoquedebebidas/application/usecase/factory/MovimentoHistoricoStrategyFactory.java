package com.magis5.estoquedebebidas.application.usecase.factory;

import com.magis5.estoquedebebidas.application.usecase.strategy.EntradaMovimentoStrategy;
import com.magis5.estoquedebebidas.application.usecase.strategy.MovimentoHistoricoStrategy;
import com.magis5.estoquedebebidas.application.usecase.strategy.SaidaMovimentoStrategy;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MovimentoHistoricoStrategyFactory {
    private final Map<TipoMovimento, MovimentoHistoricoStrategy> strategies;

    public MovimentoHistoricoStrategyFactory(List<MovimentoHistoricoStrategy> strategyList) {
        this.strategies = new HashMap<>();

        for (MovimentoHistoricoStrategy strategy : strategyList) {
            if(strategy instanceof EntradaMovimentoStrategy) {
                strategies.put(TipoMovimento.ENTRADA, strategy);
            } else if (strategy instanceof SaidaMovimentoStrategy) {
                strategies.put(TipoMovimento.SAIDA, strategy);
            }
        }
    }
    public MovimentoHistoricoStrategy getStrategy(TipoMovimento tipoMovimento) {
        return strategies.get(tipoMovimento);
    }

}
