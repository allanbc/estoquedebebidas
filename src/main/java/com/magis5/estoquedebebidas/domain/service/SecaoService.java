package com.magis5.estoquedebebidas.domain.service;

import com.magis5.estoquedebebidas.core.exceptions.RecebeBebidaAlcoolicaException;
import com.magis5.estoquedebebidas.data.models.SecaoDTO;
import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.core.exceptions.SecaoNotFoundException;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.usecase.strategy.movements.MovimentacaoBebidas;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class SecaoService {

    private static final Logger LOG = getLogger(SecaoService.class);
    @PersistenceContext
    private final EntityManager manager;
    private final BebidaService bebidaService;
    private final MovimentacaoBebidas movimentacaoBebidas;
    private final HistoricoService historicoService;

    public SecaoService(EntityManager manager, BebidaService bebidaService, MovimentacaoBebidas movimentacaoBebidas, HistoricoService historicoService) {
        this.manager = manager;
        this.bebidaService = bebidaService;
        this.movimentacaoBebidas = movimentacaoBebidas;
        this.historicoService = historicoService;
    }

    public Secao getBySecaoId(Long secaoId) {
        LOG.info("Buscando de seção pelo id {}", secaoId);
        return Optional.ofNullable(manager.find(Secao.class, secaoId))
                .map(Secao::secaoBuilder)
                .orElseThrow(() -> new SecaoNotFoundException(secaoId));
    }
    @Transactional
    public Secao criarSecao(SecaoDTO secaoDTO) {
        Assert.isTrue(!secaoDTO.validaPayload(), "Algumas regras de cadastro não foram obedecidas!");
        if (secaoDTO.numero() <= 0) {
            throw new SecaoInvalidaException("O número da seção deve ser maior que zero.");
        }
        if (secaoDTO.capacidadeMaxima() < secaoDTO.volumeAtual()) {
            throw new SecaoInvalidaException("A capacidade máxima não pode ser menor que o volume atual.");
        }
        List<TipoBebida> tipos = Arrays.asList(TipoBebida.values());
        if(!tipos.contains(secaoDTO.tipoBebida())) {
            throw new SecaoInvalidaException("Uma seção não pode ter dois ou mais tipos diferentes de bebidas que não seja 'ALCOOLICA e NAO ALCOOLICA");
        }

        var secao = Secao.builder()
                .numSecao(secaoDTO.numero())
                .tipoBebida(secaoDTO.tipoBebida())
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

        boolean recebeuBebidas = historicoService.verificarSePodeCadastrarBebidaSecao(bebida.getTipoBebida().name(), secaoId);

        if(!recebeuBebidas) {
            throw new RecebeBebidaAlcoolicaException(bebida.getTipoBebida());
        }

        /*
         * Aqui cada classe cuidará de um tipo específico de movimento e,
         * se o tipo não for correspondente, ela delega para o próximo na cadeia
         */
        //Cadeia de Responsabilidade
        movimentacaoBebidas.realizarMovimento(secao, bebida, tipoMovimento, responsavel, volume);

        historicoService.atualizarHistorico(secao, bebida, tipoMovimento, responsavel, volume);

    }
    public void retirarBebida(Long secaoId, Long bebidaId, String responsavel, TipoMovimento tipoMovimento, Double volume) {
        var secao = getBySecaoId(secaoId);
        var bebida = bebidaService.getByBebidaId(bebidaId);

        movimentacaoBebidas.realizarMovimento(secao, bebida, tipoMovimento, responsavel, volume);

        historicoService.atualizarHistorico(secao, bebida, tipoMovimento, responsavel, volume);

    }
}
