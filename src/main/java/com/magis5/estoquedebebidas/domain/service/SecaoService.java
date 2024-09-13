package com.magis5.estoquedebebidas.domain.service;

import com.magis5.estoquedebebidas.application.dto.SecaoDTO;
import com.magis5.estoquedebebidas.application.usecase.factory.MovimentoHistoricoStrategyFactory;
import com.magis5.estoquedebebidas.application.usecase.strategy.MovimentacaoBebidas;
import com.magis5.estoquedebebidas.application.usecase.strategy.MovimentoHistoricoStrategy;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.exception.*;
import com.magis5.estoquedebebidas.domain.model.Bebida;
import com.magis5.estoquedebebidas.domain.model.Secao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SecaoService {

    private static final Logger LOG = getLogger(SecaoService.class);
    @PersistenceContext
    private final EntityManager manager;
    private final BebidaService bebidaService;
    private final MovimentoHistoricoStrategyFactory strategyFactory;
    private final MovimentacaoBebidas movimentacaoBebidas;

    public SecaoService(EntityManager manager, BebidaService bebidaService, MovimentoHistoricoStrategyFactory strategyFactory, MovimentacaoBebidas movimentacaoBebidas) {
        this.manager = manager;
        this.bebidaService = bebidaService;
        this.strategyFactory = strategyFactory;
        this.movimentacaoBebidas = movimentacaoBebidas;
    }

    public Secao getBySecaoId(Long secaoId) {
        LOG.info("Buscando de seção pelo id {}", secaoId);
        return Optional.ofNullable(manager.find(Secao.class, secaoId))
                .map(Secao::secaoBuilder)
                .orElseThrow(() -> new SecaoNotFoundException(secaoId));
    }
    @Transactional
    public Secao criarSecao(SecaoDTO secaoDTO) {

        if (secaoDTO.numero() <= 0) {
            throw new SecaoInvalidaException("O número da seção deve ser maior que zero.");
        }
        if (secaoDTO.capacidadeMaxima() < secaoDTO.volumeAtual()) {
            throw new SecaoInvalidaException("A capacidade máxima não pode ser menor que o volume atual.");
        }

        var secao = Secao.builder()
                .numSecao(secaoDTO.numero())
                .tipoDeBebida(secaoDTO.tipoBebida())
                .capacidadeMaxima(secaoDTO.capacidadeMaxima())
                .volumeAtual(secaoDTO.volumeAtual())
                .build();

         manager.persist(secao);
         return secao;
    }

    /**
     * @param secaoId indica o identificador do produto, ou seja, em qual seção a bebida está sendo registrada
     * @param bebidaId o id do produto propriamente dito
     * @param tipoMovimento se é de ENTRADA ou SAÍDA
     * @param responsavel a pessoa que registra na seção a bebida que entrou no depósito
     * @param volume a quantidade de bebidas armazenadas no depósito por seção
     */
    public void adicionarBebida(Long secaoId, Long bebidaId, String responsavel, TipoMovimento tipoMovimento, Double volume) {
        var secao = getBySecaoId(secaoId);
        var bebida = bebidaService.getByBebidaId(bebidaId);

        secao.validarAdicionarBebida(bebida, secao, volume);

        /*
         * Aqui cada classe cuidará de um tipo específico de movimento e,
         * se o tipo não for correspondente, ela delega para o próximo na cadeia
         */
        movimentacaoBebidas.realizarMovimento(secao, bebida, tipoMovimento, responsavel, volume);
        atualizarHistorico(secao, bebida, tipoMovimento, responsavel, volume);

    }
    public void atualizarHistorico(Secao secao, Bebida bebida, TipoMovimento tipoMovimento, String responsavel, Double volume) {

        MovimentoHistoricoStrategy strategy = strategyFactory.getStrategy(tipoMovimento);
        if (strategy == null) {
            throw new MovimentoInvalidoException(tipoMovimento.name());
        }
        strategy.registrar(secao, bebida, tipoMovimento, responsavel, volume);
    }
}
