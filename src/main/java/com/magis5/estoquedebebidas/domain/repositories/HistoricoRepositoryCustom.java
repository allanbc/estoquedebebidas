package com.magis5.estoquedebebidas.domain.repositories;

import com.magis5.estoquedebebidas.adapters.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Historico;
import com.magis5.estoquedebebidas.domain.entities.Secao;

import java.util.List;
public interface HistoricoRepositoryCustom {
    List<Historico> findHistorico(String sortField, String sortDirection);

    boolean verificarSePodeCadastrarBebidaSecao(String tipoBebida, Long secaoId);

    void atualizarHistorico(Secao pesquisaSecao, Secao secao, Bebida bebida, MovimentoBebidasRequest request);
}
