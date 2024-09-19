package com.magis5.estoquedebebidas.domain.validators.implementations;

import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.data.models.SecaoDTO;

/*
    Implementação concreta do validador
 */
public class NumSecaoValidator extends AbstractSecaoValidator{
    @Override
    protected void handleValidarRegra(SecaoDTO secaoDTO) throws SecaoInvalidaException {
        if (secaoDTO.numero() <= 0) {
            throw new SecaoInvalidaException("O número da seção deve ser maior que zero.");
        }
        passToNext(secaoDTO);
    }
}
