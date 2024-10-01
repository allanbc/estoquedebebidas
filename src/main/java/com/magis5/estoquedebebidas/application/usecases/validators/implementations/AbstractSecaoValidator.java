package com.magis5.estoquedebebidas.application.usecases.validators.implementations;

import com.magis5.estoquedebebidas.application.usecases.validators.interfaces.SecaoValidator;
import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.adapters.models.SecaoDTO;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


public abstract class AbstractSecaoValidator implements SecaoValidator {
    protected SecaoValidator nextValidator;

    @PersistenceContext
    protected EntityManager manager;

    @PostConstruct
    public void init() {
        // Lógica de inicialização que pode usar o EntityManager
        if (manager == null) {
            throw new IllegalStateException("EntityManager não foi injetado.");
        }
    }
    @Override
    public void validate(SecaoDTO secaoDTO) throws SecaoInvalidaException {
        /*
            Aqui está o fluxo principal dessa cadeia de validação
            Primeiro, o método chama handleValidarRegra(secaoDTO), que é um método abstrato.
            Cada subclasse precisa implementar essa lógica para definir sua própria regra de validação.
            Depois, ele chama passToNext(secaoDTO), que apenas passa a execução para o próximo validador (se existir).
         */
        handleValidarRegra(secaoDTO);

        passToNext(secaoDTO);
    }

    protected void passToNext(SecaoDTO secaoDTO) throws SecaoInvalidaException {
        if (nextValidator != null) {
            nextValidator.validate(secaoDTO);
        }
    }

    /**
     *
     * @param next define qual será o próximo validador na cadeia
     * nexValidator armazena o próximo validador na cadeia
     */
    @Override
    public void setNext(SecaoValidator next) {
        this.nextValidator = next;
    }

    protected abstract void handleValidarRegra(SecaoDTO secaoDTO) throws SecaoInvalidaException;
}
