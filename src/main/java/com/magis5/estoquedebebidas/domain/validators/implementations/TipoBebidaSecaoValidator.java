package com.magis5.estoquedebebidas.domain.validators.implementations;

import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.data.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;

import java.util.Arrays;
import java.util.List;

public class TipoBebidaSecaoValidator extends AbstractSecaoValidator{
    @Override
    protected void handleValidarRegra(SecaoDTO secaoDTO) throws SecaoInvalidaException {
            List<TipoBebida> tipos = Arrays.asList(TipoBebida.values());
            if (!tipos.contains(secaoDTO.tipoBebida())) {
                throw new SecaoInvalidaException("Uma seção não pode ter dois ou mais tipos diferentes de bebidas que não seja 'ALCOOLICA e NAO ALCOOLICA'");
            }
            passToNext(secaoDTO);
    }
}
