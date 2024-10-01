package com.magis5.estoquedebebidas.application.usecases.strategies.implementations;

import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.application.usecases.chains.ValidacaoEntrada;

public class ValidacaoEntradaVolumeEstoque implements ValidacaoEntrada {

    @Override
    public boolean validar(Bebida bebida, TipoBebida tipoBebida, Secao secao, double volume) {
        return secao.getVolumeAtual() >= volume || secao.getVolumeAtual() >= 0;
    }
}
