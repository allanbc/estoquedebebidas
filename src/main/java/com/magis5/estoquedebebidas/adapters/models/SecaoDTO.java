package com.magis5.estoquedebebidas.adapters.models;

import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;

public record SecaoDTO(
        int numero,
        TipoBebida tipoBebida,
        double capacidadeMaxima,
        double volume
        ) {

        public SecaoDTO(Secao secao) {
                this(secao.getNumSecao(), secao.getTipoBebida(), secao.getCapacidadeMaxima(), secao.getVolumeAtual());
        }

        public boolean validaPayload() {
                return tipoBebida == null && volume <= 0 && capacidadeMaxima > volume && numero > 0;
        }
}
