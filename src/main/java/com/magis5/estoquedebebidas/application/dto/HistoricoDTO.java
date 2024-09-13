package com.magis5.estoquedebebidas.application.dto;

import java.time.LocalDateTime;

/**
 * @param tipoMovimento Entrada ou Saída
 */
public record HistoricoDTO(
        String nomeBebida,
        int numeroSecao,
        double volume,
        String tipoMovimento,
        LocalDateTime dataHora,
        String responsavel
) {
}
