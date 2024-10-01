package com.magis5.estoquedebebidas.application.usecases.chains.implementations;

import com.magis5.estoquedebebidas.application.usecases.validators.implementations.EntradaMovimentoHandlerChain;
import com.magis5.estoquedebebidas.application.usecases.validators.implementations.SaidaMovimentoHandlerChain;
import com.magis5.estoquedebebidas.application.usecases.validators.interfaces.MovimentoHandlerChain;
import com.magis5.estoquedebebidas.adapters.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import org.springframework.stereotype.Component;

/**
 * Aqui é montada a cadeia de responsabilidades, ligando os diferentes handlers.
 * A cadeia pode ser configurada uma vez e reutilizada.
 */
@Component
public class MovimentacaoBebidas {
    /**
     * Adicionando Chain no Strategy pra minimizar o uso de IFs
     */
    private final MovimentoHandlerChain chain;

    public MovimentacaoBebidas() {
        /*
            Nessa etapa é construída a cadeia de responsabilidade
         */
        MovimentoHandlerChain entradaHandler = new EntradaMovimentoHandlerChain();
        MovimentoHandlerChain saidaHandler = new SaidaMovimentoHandlerChain();

        entradaHandler.setNext(saidaHandler);
        this.chain = entradaHandler; //Define o início da cadeia
    }

    /**
     * @param secao indica em qual seção a bebida está sendo registrada
     * @param bebida o produto propriamente dito
     * @param request dto
     */
    public void realizarMovimento(Secao secao, Bebida bebida, MovimentoBebidasRequest request) {
        chain.handle(secao, bebida, request);
    }
}
