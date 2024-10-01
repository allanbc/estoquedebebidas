package com.magis5.estoquedebebidas.application.usecases.validators.implementations;

import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.adapters.models.SecaoDTO;
import com.magis5.estoquedebebidas.application.usecases.validators.interfaces.SecaoValidator;
import org.springframework.stereotype.Component;

@Component
public class SecaoValidadorChain {
    private final SecaoValidator validator;
    private final NumSecaoValidator numeroValidator;
    private final CapacidadeSecaoValidator capacidadeValidator;
    private final TipoBebidaSecaoValidator tipoBebidaValidator;


    public SecaoValidadorChain(NumSecaoValidator numeroValidator,
                               CapacidadeSecaoValidator capacidadeValidator,
                               TipoBebidaSecaoValidator tipoBebidaValidator) {

        this.numeroValidator = numeroValidator;
        this.capacidadeValidator = capacidadeValidator;
        this.tipoBebidaValidator = tipoBebidaValidator;
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

        numeroValidator.setNext(capacidadeValidator);
        capacidadeValidator.setNext(tipoBebidaValidator);
        this.validator = numeroValidator;
    }

    public void validate(SecaoDTO secaoDTO) throws SecaoInvalidaException {
        validator.validate(secaoDTO);
    }
}

