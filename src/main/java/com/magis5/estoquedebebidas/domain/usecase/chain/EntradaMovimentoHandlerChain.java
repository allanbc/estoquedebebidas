package com.magis5.estoquedebebidas.domain.usecase.chain;

import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class EntradaMovimentoHandlerChain implements MovimentoHandlerChain{

    private static final Logger LOG = getLogger(EntradaMovimentoHandlerChain.class);

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
        if(tipoMovimento == TipoMovimento.ENTRADA) {
            LOG.info(String.format("Registrando entrada: %S, Volume: %2f", bebida.getNome(), volume));
        } else if(nextChain != null) {
            nextChain.handle(secao, bebida, tipoMovimento, responsavel, volume);
        }
    }
}
