package com.magis5.estoquedebebidas.domain.validators.implementations;

import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.data.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;

public class CapacidadeSecaoValidator extends AbstractSecaoValidator {
    @Override
    protected void handleValidarRegra(SecaoDTO secaoDTO) throws SecaoInvalidaException {
        double capacidade = secaoDTO.tipoBebida().equals(TipoBebida.ALCOOLICA) ? 500.0 : 400.0;
        if (secaoDTO.capacidadeMaxima() < secaoDTO.volume() || secaoDTO.capacidadeMaxima() > capacidade) {
            throw new SecaoInvalidaException("A capacidade máxima não pode ser menor que o volume atual nem maior que a capacidade de armazenamento da seção para o tipo de bebida " + secaoDTO.tipoBebida().name());
        }
        passToNext(secaoDTO);
    }
}
