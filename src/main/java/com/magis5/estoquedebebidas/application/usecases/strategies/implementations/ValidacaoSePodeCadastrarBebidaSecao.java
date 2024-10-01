package com.magis5.estoquedebebidas.application.usecases.strategies.implementations;

import com.magis5.estoquedebebidas.application.services.HistoricoService;
import com.magis5.estoquedebebidas.application.usecases.chains.ValidacaoEntrada;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.repositories.HistoricoRepositoryCustom;

public class ValidacaoSePodeCadastrarBebidaSecao implements ValidacaoEntrada {
    private final HistoricoRepositoryCustom historicoRepositoryCustom;

    public ValidacaoSePodeCadastrarBebidaSecao(HistoricoRepositoryCustom historicoRepositoryCustom) {
        this.historicoRepositoryCustom = historicoRepositoryCustom;
    }

    @Override
    public boolean validar(Bebida bebidaDto, TipoBebida tipoBebida, Secao secao, double volume) {
        return historicoRepositoryCustom.verificarSePodeCadastrarBebidaSecao(bebidaDto.getTipoBebida().name(), secao.getId());
    }
}
