package com.magis5.estoquedebebidas.domain.usecase.chain;

import com.magis5.estoquedebebidas.core.exceptions.AdicionarBebidaException;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.service.SecaoService;
import com.magis5.estoquedebebidas.domain.usecase.strategy.validations.ValidacaoEntrada;

import java.util.List;

public class ValidacaoEstoquehandler extends AbstractHandler {

    private final List<ValidacaoEntrada> validacoes;
    private final SecaoService secaoService;

    public ValidacaoEstoquehandler(List<ValidacaoEntrada> validacoes, SecaoService secaoService) {
        this.validacoes = validacoes;
        this.secaoService = secaoService;
    }
    @Override
    public void handle(Bebida bebida, double volume, String responsavel, TipoMovimento tipoMovimento) {

        Secao secao = obterSecao(bebida.getSecao().getId());

        for (ValidacaoEntrada validacao : validacoes) {
            if (!validacao.validar(bebida, bebida.getTipoBebida(), secao, volume)) {
                throw new AdicionarBebidaException(bebida.getNome());
            }
        }
        // Passa para o próximo handler na cadeia
            passToNext(bebida, volume, responsavel, tipoMovimento);
        }
        private Secao obterSecao(Long secaoId) {
            return secaoService.getBySecaoId(secaoId);
        }

        // Método para validar o estoque disponível no movimento de saída
        private boolean validarSaidaEstoque(Bebida bebida, Secao secao, double volume) {
            double estoqueDisponivel = secao.getVolumeAtual();
            return volume <= estoqueDisponivel;
        }

}
