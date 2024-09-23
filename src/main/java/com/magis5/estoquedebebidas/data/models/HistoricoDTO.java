package com.magis5.estoquedebebidas.data.models;

import com.magis5.estoquedebebidas.domain.entities.Historico;

import java.time.LocalDateTime;

/**
 * @param tipoMovimento Entrada ou Saída
 */
public record HistoricoDTO(
        Long bebidaId,
        Long secaoId,
        double volume,
        String tipoMovimento,
        LocalDateTime dataHora,
        String responsavel
) {
    public static Historico convertToDTO(Historico historico) {
        return Historico.builder()
                .bebida(historico.getBebida())
                .secao(historico.getSecao())
                .volume(historico.getVolume())
                .dataHora(historico.getDataHora())
                .responsavel(historico.getResponsavel())
                .tipoMovimento(historico.getTipoMovimento())
                .build();
    }
}
