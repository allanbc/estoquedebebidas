package com.magis5.estoquedebebidas.application.usecase.strategy;

import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.exception.SecaoNotFoundException;
import com.magis5.estoquedebebidas.domain.model.Bebida;
import com.magis5.estoquedebebidas.domain.model.Historico;
import com.magis5.estoquedebebidas.domain.model.Secao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SaidaMovimentoStrategy implements MovimentoHistoricoStrategy {

    @PersistenceContext
    EntityManager manager;
    @Override
    public void registrar(Secao secao, Bebida bebida, TipoMovimento tipoMovimento, String responsavel, Double volume) {
        Secao pesquisaSecao = Optional.ofNullable(manager.find(Secao.class, secao.getId()))
//                .map(Secao::secaoBuilder)
                .orElseThrow(() -> new SecaoNotFoundException(secao.getId()));

        if (pesquisaSecao != null && secao.getVolumeAtual() >= secao.getVolumeAtual()) {
            // Atualiza o estoque
            pesquisaSecao.setVolumeAtual(pesquisaSecao.getVolumeAtual() - volume);
            // Adiciona ao histórico
            Historico historico = Historico.builder()
                    .secao(pesquisaSecao)
                    .bebida(bebida)
                    .volume(volume)
                    .tipoMovimento(TipoMovimento.SAIDA)
                    .responsavel(responsavel)
                    .build();

            manager.persist(historico);
        } else {
            throw new IllegalArgumentException("Volume de saída inválido ou seção não encontrada.");
        }
    }
}
