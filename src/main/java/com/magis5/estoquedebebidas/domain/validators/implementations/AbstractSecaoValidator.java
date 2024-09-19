package com.magis5.estoquedebebidas.domain.validators.implementations;

import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.data.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.validators.interfaces.SecaoValidator;

public abstract class AbstractSecaoValidator implements SecaoValidator {
    protected SecaoValidator next;
    @Override
    public void validate(SecaoDTO secaoDTO) throws SecaoInvalidaException {
        if(next != null) {
            next.validate(secaoDTO);
        }
    }

    @Override
    public void setNext(SecaoValidator next) {
        this.next = next;
    }

    protected abstract void handleValidation(SecaoDTO secaoDTO) throws SecaoInvalidaException;
}
