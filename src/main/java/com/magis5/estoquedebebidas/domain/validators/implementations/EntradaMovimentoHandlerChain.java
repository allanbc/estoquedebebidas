package com.magis5.estoquedebebidas.domain.validators.implementations;

import com.magis5.estoquedebebidas.data.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.validators.interfaces.MovimentoHandlerChain;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class EntradaMovimentoHandlerChain implements MovimentoHandlerChain {

    private static final Logger LOG = getLogger(EntradaMovimentoHandlerChain.class);

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
        if(request.getTipoMovimento() == TipoMovimento.ENTRADA) {
            LOG.info(String.format("Registrando entrada: %S, Volume: %2f", bebida.getNome(), request.getVolume()));
        } else if(nextChain != null) {
            nextChain.handle(secao, bebida, request);
        }
    }
}
