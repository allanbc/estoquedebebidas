package com.magis5.estoquedebebidas.domain.usecase.strategy.validations;

import com.magis5.estoquedebebidas.core.exceptions.SaidaEstoqueException;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.service.SecaoService;
import com.magis5.estoquedebebidas.domain.usecase.chain.AbstractHandler;

import java.util.List;

public class ValidacaoSaidaHandler extends AbstractHandler {

    private final List<ValidacaoSaida> validacoes;
    private final SecaoService secaoService;

    public ValidacaoSaidaHandler(List<ValidacaoSaida> validacoes, SecaoService secaoService) {
        this.validacoes = validacoes;
        this.secaoService = secaoService;
    }

    @Override
    public void handle(Bebida bebida, double volume, String responsavel, TipoMovimento tipoMovimento) {
        Secao secao = obterSecao(bebida.getSecao().getId());

        for (ValidacaoSaida validacao : validacoes) {
            if (!validacao.validar(bebida, bebida.getTipoBebida(), secao, volume)) {
                throw new SaidaEstoqueException(bebida.getNome());
            }
        }
        passToNext(bebida, volume, responsavel, tipoMovimento);
    }

    private Secao obterSecao(Long secaoId) {
        return secaoService.getBySecaoId(secaoId);
    }

}

