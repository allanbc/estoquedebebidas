package com.magis5.estoquedebebidas.domain.repositories;

import com.magis5.estoquedebebidas.domain.entities.Historico;

import java.util.List;
public interface HistoricoRepositoryCustom {
    List<Historico> findHistorico(String sortField, String sortDirection);
}
