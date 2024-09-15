package com.magis5.estoquedebebidas.data.models;

import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;

public record SecaoDTO(
        int numero,
        TipoBebida tipoBebida,
        double capacidadeMaxima,
        double volumeAtual
        ) {

        public SecaoDTO(Secao secao) {
                this(secao.getNumSecao(), secao.getTipoDeBebida(), secao.getCapacidadeMaxima(), secao.getVolumeAtual());
        }

        public boolean validaPayload() {
                return tipoBebida == null && volumeAtual <= 0 && capacidadeMaxima > volumeAtual && numero > 0;
        }
}
