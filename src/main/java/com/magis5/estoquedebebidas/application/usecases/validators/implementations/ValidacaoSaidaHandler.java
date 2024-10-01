package com.magis5.estoquedebebidas.application.usecases.validators.implementations;

import com.magis5.estoquedebebidas.application.usecases.chains.ValidacaoSaida;
import com.magis5.estoquedebebidas.core.exceptions.SaidaEstoqueException;
import com.magis5.estoquedebebidas.adapters.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.application.services.SecaoService;

import java.util.List;

public class ValidacaoSaidaHandler extends AbstractHandler {

    private final List<ValidacaoSaida> validacoes;
    private final SecaoService secaoService;

    public ValidacaoSaidaHandler(List<ValidacaoSaida> validacoes, SecaoService secaoService) {
        this.validacoes = validacoes;
        this.secaoService = secaoService;
    }

    @Override
    public void handle(Bebida bebida, MovimentoBebidasRequest request) {
        Secao secao = obterSecao(bebida.getSecao().getId());

        for (ValidacaoSaida validacao : validacoes) {
            if (!validacao.validar(bebida, bebida.getTipoBebida(), secao, request.volume())) {
                throw new SaidaEstoqueException(bebida.getNome());
            }
        }
        passToNext(bebida, request);
    }

    private Secao obterSecao(Long secaoId) {
        return secaoService.getBySecaoId(secaoId);
    }

}

