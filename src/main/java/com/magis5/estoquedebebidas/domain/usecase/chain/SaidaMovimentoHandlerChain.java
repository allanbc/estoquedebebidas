package com.magis5.estoquedebebidas.domain.usecase.chain;

import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;

public class SaidaMovimentoHandlerChain implements MovimentoHandlerChain {

    private MovimentoHandlerChain nextChain;
    @Override
    public void setNext(MovimentoHandlerChain handler) {
        this.nextChain = handler;
    }

    /**
     * @param secao indica em qual seção a bebida está sendo registrada
     * @param bebida o produto propriamente dito
     * @param tipoMovimento se é de ENTRADA ou SAÍDA
     * @param responsavel a pessoa que registra na seção a bebida que entrou no depósito
     * @param volume a quantidade de bebidas armazenadas no depósito por seção
     */
    @Override
    public void handle(Secao secao, Bebida bebida, TipoMovimento tipoMovimento, String responsavel, Double volume) {
        if (tipoMovimento == TipoMovimento.SAIDA) {
            System.out.println("Registrando saída: " + bebida.getNome() + ", Volume: " + volume);
        } else if (nextChain != null) {
            nextChain.handle(secao, bebida, tipoMovimento, responsavel, volume);
        }
    }
}
