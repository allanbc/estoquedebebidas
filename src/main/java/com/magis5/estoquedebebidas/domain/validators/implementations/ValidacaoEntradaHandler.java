package com.magis5.estoquedebebidas.domain.validators.implementations;

import com.magis5.estoquedebebidas.core.exceptions.AdicionarBebidaException;
import com.magis5.estoquedebebidas.data.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.service.SecaoService;
import com.magis5.estoquedebebidas.domain.usecase.chains.ValidacaoEntrada;

import java.util.List;

public class ValidacaoEntradaHandler extends AbstractHandler {

    private final List<ValidacaoEntrada> validacoes;
    private final SecaoService secaoService;

    public ValidacaoEntradaHandler(List<ValidacaoEntrada> validacoes, SecaoService secaoService) {
        this.validacoes = validacoes;
        this.secaoService = secaoService;
    }
    @Override
    public void handle(Bebida bebida, MovimentoBebidasRequest request) {

        Secao secao = obterSecao(bebida.getSecao().getId());

        for (ValidacaoEntrada validacao : validacoes) {
            if (!validacao.validar(bebida, bebida.getTipoBebida(), secao, request.getVolume())) {
                throw new AdicionarBebidaException(bebida.getNome(),  validacao.getClass().getSimpleName());
            }
        }
        // Passa para o próximo handler na cadeia
            passToNext(bebida, request);
        }
        private Secao obterSecao(Long secaoId) {
            return secaoService.getBySecaoId(secaoId);
        }
}
