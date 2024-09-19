package com.magis5.estoquedebebidas.domain.usecase.strategies.implementations;

import com.magis5.estoquedebebidas.data.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.core.exceptions.SecaoNotFoundException;
import com.magis5.estoquedebebidas.core.exceptions.VolumeSecaoInvalidaException;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.repositories.HistoricoRepositoryCustom;
import com.magis5.estoquedebebidas.domain.usecase.strategies.interfaces.MovimentoHistoricoStrategy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EntradaMovimentoStrategy implements MovimentoHistoricoStrategy {
    @PersistenceContext
    private final EntityManager manager;

    private final HistoricoRepositoryCustom historicoRepositoryCustom;

    public EntradaMovimentoStrategy(EntityManager manager, HistoricoRepositoryCustom historicoRepositoryCustom) {
        this.manager = manager;
        this.historicoRepositoryCustom = historicoRepositoryCustom;
    }

    @Transactional
    public void registrar(Secao secao, Bebida bebida, MovimentoBebidasRequest request) {
        Secao pesquisaSecao = Optional.ofNullable(manager.find(Secao.class, secao.getId()))
                .orElseThrow(() -> new SecaoNotFoundException(secao.getId()));

        if (pesquisaSecao != null && (secao.getVolumeAtual() + request.getVolume() <= secao.getCapacidadeMaxima())) {
            // Atualiza o estoque
            pesquisaSecao.setVolumeAtual(pesquisaSecao.getVolumeAtual() + request.getVolume());
            historicoRepositoryCustom.atualizarHistorico(pesquisaSecao, secao, bebida, request);
        } else {
            throw new VolumeSecaoInvalidaException(secao.getId());
        }
    }
}
