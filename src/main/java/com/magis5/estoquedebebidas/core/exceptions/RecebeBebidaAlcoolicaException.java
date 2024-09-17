package com.magis5.estoquedebebidas.core.exceptions;

import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import org.springframework.http.HttpStatus;

public class RecebeBebidaAlcoolicaException extends EstoqueBebidasException {
    public RecebeBebidaAlcoolicaException(TipoBebida tipoBebida) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "secao_recebe_bebida_exception",
                "Não é possível armazenar essa bebida, pois uma seção não pode receber bebidas NÃO ALCOOLICAS " +
                        "se recebeu 'ALCOOLICAS' no mesmo dia ");
    }
}
