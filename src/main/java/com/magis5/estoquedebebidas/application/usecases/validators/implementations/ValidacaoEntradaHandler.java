package com.magis5.estoquedebebidas.application.usecases.validators.implementations;

import com.magis5.estoquedebebidas.application.usecases.chains.ValidacaoEntrada;
import com.magis5.estoquedebebidas.application.usecases.strategies.implementations.ValidacaoEntradaTipoBebida;
import com.magis5.estoquedebebidas.application.usecases.strategies.implementations.ValidacaoEntradaVolumeEstoque;
import com.magis5.estoquedebebidas.application.usecases.strategies.implementations.ValidacaoSePodeCadastrarBebidaSecao;
import com.magis5.estoquedebebidas.core.exceptions.AdicionarBebidaException;
import com.magis5.estoquedebebidas.adapters.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.core.exceptions.RecebeBebidaAlcoolicaException;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.application.services.SecaoService;

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
            if (!validacao.validar(bebida, bebida.getTipoBebida(), secao, request.volume())) {

                if(validacao instanceof ValidacaoEntradaVolumeEstoque || validacao instanceof ValidacaoEntradaTipoBebida) {
                    throw new AdicionarBebidaException(bebida.getNome(),  validacao.getClass().getSimpleName());
                }
                if(validacao instanceof ValidacaoSePodeCadastrarBebidaSecao) {
                    throw new RecebeBebidaAlcoolicaException(bebida.getTipoBebida());
                }

            }
        }
        // Passa para o próximo handler na cadeia
            passToNext(bebida, request);
        }
        private Secao obterSecao(Long secaoId) {
            return secaoService.getBySecaoId(secaoId);
        }
}
