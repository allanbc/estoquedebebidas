package com.magis5.estoquedebebidas.application.usecases.validators.implementations;

import com.magis5.estoquedebebidas.adapters.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.application.usecases.validators.interfaces.MovimentoHandlerChain;

import java.text.DecimalFormat;

public class SaidaMovimentoHandlerChain implements MovimentoHandlerChain {

    private MovimentoHandlerChain nextChain;
    @Override
    public void setNext(MovimentoHandlerChain handler) {
        this.nextChain = handler;
    }

    /**
     * @param secao indica em qual seção a bebida está sendo registrada
     * @param bebida o produto propriamente dito
     * @param request dto
     */
    @Override
    public void handle(Secao secao, Bebida bebida, MovimentoBebidasRequest request) {
        if (request.tipoMovimento() == TipoMovimento.SAIDA) {
            System.out.println("Registrando saída: " + bebida.getNome() + ", Volume: " + new DecimalFormat("#,##0.0").format(request.volume()));
        } else if (nextChain != null) {
            nextChain.handle(secao, bebida, request);
        }
    }
}
