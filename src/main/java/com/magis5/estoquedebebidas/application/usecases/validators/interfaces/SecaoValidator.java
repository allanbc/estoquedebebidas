package com.magis5.estoquedebebidas.application.usecases.validators.interfaces;

import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.adapters.models.SecaoDTO;

public interface SecaoValidator {
    void validate(SecaoDTO secaoDTO) throws SecaoInvalidaException;
    void setNext(SecaoValidator next);
}
