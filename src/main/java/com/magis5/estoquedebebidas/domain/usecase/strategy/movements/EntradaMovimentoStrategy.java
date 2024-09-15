package com.magis5.estoquedebebidas.domain.usecase.strategy.movements;

import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.core.exceptions.SecaoNotFoundException;
import com.magis5.estoquedebebidas.core.exceptions.VolumeSecaoInvalidaException;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Historico;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EntradaMovimentoStrategy implements MovimentoHistoricoStrategy {
    @PersistenceContext
    EntityManager manager;

    @Transactional
    @Override
    public void registrar(Secao secao, Bebida bebida, TipoMovimento tipoMovimento, String responsavel, Double volume) {
        Secao pesquisaSecao = Optional.ofNullable(manager.find(Secao.class, secao.getId()))
//                .map(Secao::secaoBuilder)
                .orElseThrow(() -> new SecaoNotFoundException(secao.getId()));

        if (pesquisaSecao != null && (secao.getVolumeAtual() + volume <= secao.getCapacidadeMaxima())) {
            // Atualiza o estoque
            pesquisaSecao.setVolumeAtual(pesquisaSecao.getVolumeAtual() + volume);

            // Adiciona ao histórico
            Historico historico = Historico.builder()
                    .secao(pesquisaSecao)
                    .bebida(bebida)
                    .volume(volume)
                    .tipoMovimento(tipoMovimento)
                    .responsavel(responsavel)
                    .build();

            manager.persist(historico);
        } else {
            throw new VolumeSecaoInvalidaException(secao.getId());
        }
    }
}
