package com.magis5.estoquedebebidas.application.usecases.validators.implementations;

import com.magis5.estoquedebebidas.adapters.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.application.usecases.validators.interfaces.MovimentoHandlerChain;
import org.slf4j.Logger;

import java.text.DecimalFormat;

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
        if(request.tipoMovimento() == TipoMovimento.ENTRADA) {
            LOG.info(String.format("Registrando entrada: %s, Volume: %s", bebida.getNome(), new DecimalFormat("#,##0.0").format(request.volume())));
        } else if(nextChain != null) {
            nextChain.handle(secao, bebida, request);
        }
    }
}
