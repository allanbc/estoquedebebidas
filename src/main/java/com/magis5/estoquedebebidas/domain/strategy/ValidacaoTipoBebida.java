package com.magis5.estoquedebebidas.domain.strategy;

import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.model.Bebida;
import com.magis5.estoquedebebidas.domain.model.Secao;

public class ValidacaoTipoBebida implements Validacao{
    @Override
    public boolean validar(Bebida bebida, TipoBebida tipoBebida, Secao secao, double volume) {
        return secao.podeArmazenar(tipoBebida.name(), secao.getVolumeAtual());
    }
}
