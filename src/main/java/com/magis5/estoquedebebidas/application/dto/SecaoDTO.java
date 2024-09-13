package com.magis5.estoquedebebidas.application.dto;

import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.model.Secao;

public record SecaoDTO(
        int numero,
        TipoBebida tipoBebida,
        double capacidadeMaxima,
        double volumeAtual
        ) {

        public SecaoDTO(Secao secao) {
                this(secao.getNumSecao(), secao.getTipoDeBebida(), secao.getCapacidadeMaxima(), secao.getVolumeAtual());
        }
}
