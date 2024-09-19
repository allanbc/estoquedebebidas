package com.magis5.estoquedebebidas.domain.validators.interfaces;

import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.data.models.SecaoDTO;

public interface SecaoValidator {
    void validate(SecaoDTO secaoDTO) throws SecaoInvalidaException;
    void setNext(SecaoValidator next);
}
