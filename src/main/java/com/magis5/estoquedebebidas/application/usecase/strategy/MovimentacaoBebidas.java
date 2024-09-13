package com.magis5.estoquedebebidas.application.usecase.strategy;

import com.magis5.estoquedebebidas.application.usecase.chain.EntradaMovimentoHandlerChain;
import com.magis5.estoquedebebidas.application.usecase.chain.MovimentoHandlerChain;
import com.magis5.estoquedebebidas.application.usecase.chain.SaidaMovimentoHandlerChain;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.model.Bebida;
import com.magis5.estoquedebebidas.domain.model.Secao;
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
     * @param tipoMovimento se é de ENTRADA ou SAÍDA
     * @param responsavel a pessoa que registra na seção a bebida que entrou no depósito
     * @param volume a quantidade de bebidas armazenadas no depósito por seção
     */
    public void realizarMovimento(Secao secao, Bebida bebida, TipoMovimento tipoMovimento,
                                  String responsavel, Double volume) {
        chain.handle(secao, bebida, tipoMovimento, responsavel, volume);
    }
}
