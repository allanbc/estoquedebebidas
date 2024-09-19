package com.magis5.estoquedebebidas.domain.usecase.strategies.implementations;

import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.usecase.chains.ValidacaoSaida;

public class ValidacaoSaidaVolumeMaiorQAtual implements ValidacaoSaida {

    @Override
    public boolean validar(Bebida bebida, TipoBebida tipoBebida, Secao secao, double volume) {
        // Valida se o volume disponível é suficiente para a saída
        return secao.getVolumeAtual() >= volume;
    }

}

