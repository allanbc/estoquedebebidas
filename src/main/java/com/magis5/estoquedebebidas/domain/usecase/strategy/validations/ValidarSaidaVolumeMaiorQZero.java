package com.magis5.estoquedebebidas.domain.usecase.strategy.validations;

import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;

public class ValidarSaidaVolumeMaiorQZero implements ValidacaoSaida{
    @Override
    public boolean validar(Bebida bebida, TipoBebida tipoBebida, Secao secao, double volume) {
        var maiorQZero = secao.removerVolumeMaiorQueZero(tipoBebida.name(), volume);
        var maiorQAtual = secao.removerVolumeMaiorQueAtual(bebida, secao, volume);
        return maiorQZero || maiorQAtual;
    }
}
