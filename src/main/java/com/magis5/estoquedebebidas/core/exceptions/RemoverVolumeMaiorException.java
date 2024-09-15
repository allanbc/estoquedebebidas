package com.magis5.estoquedebebidas.core.exceptions;

import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import org.springframework.http.HttpStatus;

public class RemoverVolumeMaiorException extends EstoqueBebidasException {
    public RemoverVolumeMaiorException(String s) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "bebida_removida_erro", String.format("Não é possível remover o volume da bebida %s na seção", s));
    }

    public RemoverVolumeMaiorException(double volume, Bebida bebida, Secao secao) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "bebida_removida_erro", String.format("Não é possível remover o volume $d da bebida %s na seção %s", volume, bebida, secao.getNumSecao()));
    }
}
