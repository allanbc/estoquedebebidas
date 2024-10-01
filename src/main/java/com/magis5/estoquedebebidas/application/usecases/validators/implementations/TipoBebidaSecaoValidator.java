package com.magis5.estoquedebebidas.application.usecases.validators.implementations;

import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.adapters.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
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
