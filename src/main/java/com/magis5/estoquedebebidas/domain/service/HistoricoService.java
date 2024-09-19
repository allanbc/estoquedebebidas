package com.magis5.estoquedebebidas.domain.service;

import com.magis5.estoquedebebidas.data.models.HistoricoDTO;
import com.magis5.estoquedebebidas.core.exceptions.MovimentoInvalidoException;
import com.magis5.estoquedebebidas.data.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Historico;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.repositories.HistoricoRepositoryCustom;
import com.magis5.estoquedebebidas.domain.usecase.chains.ValidacaoEntrada;
import com.magis5.estoquedebebidas.domain.usecase.chains.ValidacaoSaida;
import com.magis5.estoquedebebidas.domain.usecase.strategies.implementations.ValidacaoEntradaTipoBebida;
import com.magis5.estoquedebebidas.domain.usecase.strategies.implementations.ValidacaoEntradaVolumeEstoque;
import com.magis5.estoquedebebidas.domain.validators.implementations.ValidacaoSaidaHandler;
import com.magis5.estoquedebebidas.domain.usecase.strategies.implementations.ValidacaoSaidaVolumeMaiorQAtual;
import com.magis5.estoquedebebidas.domain.usecase.strategies.implementations.ValidarSaidaVolumeMaiorQZero;
import com.magis5.estoquedebebidas.domain.validators.implementations.AbstractHandler;
import com.magis5.estoquedebebidas.domain.validators.implementations.ValidacaoEntradaHandler;
import com.magis5.estoquedebebidas.domain.usecase.factories.MovimentoHistoricoStrategyFactory;
import com.magis5.estoquedebebidas.domain.usecase.strategies.interfaces.MovimentoHistoricoStrategy;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class HistoricoService {
    private static final Logger LOG = getLogger(HistoricoService.class);
    private final HistoricoRepositoryCustom historicoRepositoryCustom;
    private final SecaoService secaoService;
    private final MovimentoHistoricoStrategyFactory strategyFactory;

    @Autowired
    private EntityManager manager;


    public HistoricoService(HistoricoRepositoryCustom historicoRepositoryCustom, @Lazy SecaoService secaoService, MovimentoHistoricoStrategyFactory strategyFactory) {
        this.historicoRepositoryCustom = historicoRepositoryCustom;
        this.secaoService = secaoService;
        this.strategyFactory = strategyFactory;
    }

    public List<Historico> consultaHistoricoOrderBySecaoDataAsc(String sortField, String sortDirection, Integer numSecao, String tipoMovimento) {
        // Recupera a lista de históricos com base na ordenação
        List<Historico> historicoList = historicoRepositoryCustom.findHistorico(sortField, sortDirection);
        return historicoList.stream()
                .filter(h -> matchesSecao(h, numSecao))
                .filter(h -> matchesTipoMovimento(h, tipoMovimento))
                .map(HistoricoDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    private boolean matchesSecao(Historico historico, Integer numSecao) {
        return Optional.of(numSecao)
                .map(secao -> secao.equals(historico.getSecao().getNumSecao()))
                .orElse(true);
    }

    private boolean matchesTipoMovimento(Historico historico, String tipoMovimento) {
        return Optional.ofNullable(tipoMovimento)
                .map(tipo -> tipo.equals(historico.getTipoMovimento().name()))
                .orElse(true);
    }

    public void atualizarHistorico(Secao secao, Bebida bebida, MovimentoBebidasRequest request) {
        AbstractHandler validacaoHandler = getAbstractHandler(request.getTipoMovimento());

        validacaoHandler.setNext(null);

        // Inicia a cadeia de manipulação
        validacaoHandler.handle(bebida, request );

        atualizar(secao, bebida, request);

    }

    /**
     *
     * @param tipoMovimento define o tipo operação, se de entrada ou saída do estoque. Se for de entrada, invoca as
     *                      operações de validação específicas para movimento de Entrada, caso contrário invoca as operações
     *                      de validação para o movimento de Saída.
     * @return handler validado
     */
    private AbstractHandler getAbstractHandler(TipoMovimento tipoMovimento) {
        AbstractHandler validacaoHandler;

        // Lista de validações
        List<ValidacaoEntrada> validacoes = Arrays.asList(new ValidacaoEntradaVolumeEstoque(), new ValidacaoEntradaTipoBebida());

        if (tipoMovimento == TipoMovimento.SAIDA) {
            List<ValidacaoSaida> validacoesSaida = Arrays.asList(new ValidarSaidaVolumeMaiorQZero(), new ValidacaoSaidaVolumeMaiorQAtual());
            validacaoHandler = new ValidacaoSaidaHandler(validacoesSaida, secaoService);
        } else {
            List<ValidacaoEntrada> validacoesEntrada = Arrays.asList(new ValidacaoEntradaVolumeEstoque(), new ValidacaoEntradaTipoBebida());
            validacaoHandler = new ValidacaoEntradaHandler(validacoesEntrada, secaoService);
        }
        return validacaoHandler;
    }

    public void atualizar(Secao secao, Bebida bebida, MovimentoBebidasRequest request) {

        MovimentoHistoricoStrategy strategy = strategyFactory.getStrategy(request.getTipoMovimento());
        if (strategy == null) {
            throw new MovimentoInvalidoException(request.getTipoMovimento().name());
        }
        strategy.registrar(secao, bebida, request);
    }

    public boolean verificarSePodeCadastrarBebidaSecao(String tipoBebida, Long secaoId) {
        return historicoRepositoryCustom.verificarSePodeCadastrarBebidaSecao(tipoBebida, secaoId);
    }
}
