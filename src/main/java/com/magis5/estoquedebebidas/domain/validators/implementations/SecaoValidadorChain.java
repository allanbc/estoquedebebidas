package com.magis5.estoquedebebidas.domain.validators.implementations;

import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.data.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.validators.interfaces.SecaoValidator;

public class SecaoValidadorChain {
    private final SecaoValidator validator;

    public SecaoValidadorChain() {
        /*
            A aplicação cria uma instância de SecaoValidadorChain.
            A cadeia de validadores é montada (o primeiro é NumSecaoValidator, depois CapacidadeSecaoValidator,
            e assim por diante).
            Quando o método validate(secaoDTO) é chamado:
             - O NumSecaoValidator valida o número da seção.
             - Se passar, ele passa a execução para o CapacidadeSecaoValidator, que valida a capacidade.
             - Se passar, ele passa a execução para o TipoBebidaSecaoValidator, que valida o tipo de bebida.
            Se todas as validações passarem, a execução termina sem exceções.
            Se qualquer validação falhar, uma exceção SecaoInvalidaException é lançada e o processo é interrompido.
         */
        NumSecaoValidator numeroValidator = new NumSecaoValidator();
        CapacidadeSecaoValidator capacidadeValidator = new CapacidadeSecaoValidator();
        TipoBebidaSecaoValidator tipoBebidaValidator = new TipoBebidaSecaoValidator();

        numeroValidator.setNext(capacidadeValidator);
        capacidadeValidator.setNext(tipoBebidaValidator);
        // Esse é o primeiro validador
        this.validator = numeroValidator;
    }

    public void validate(SecaoDTO secaoDTO) throws SecaoInvalidaException {
        validator.validate(secaoDTO);
    }
}

