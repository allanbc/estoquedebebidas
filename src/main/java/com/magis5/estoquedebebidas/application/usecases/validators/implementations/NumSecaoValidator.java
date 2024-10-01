package com.magis5.estoquedebebidas.application.usecases.validators.implementations;

import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.adapters.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/*
    Implementação concreta do validador
 */
@Component
public class NumSecaoValidator extends AbstractSecaoValidator{
    private static final Logger LOG = getLogger(NumSecaoValidator.class);

    @Override
    protected void handleValidarRegra(SecaoDTO secaoDTO) throws SecaoInvalidaException {
        LOG.info("Buscando de seção pelo número: {} ", secaoDTO.numero());

        // Verificar se a seção já existe no banco de dados
        var secaoExistente = manager.createQuery("SELECT s FROM Secao s WHERE s.numSecao = :secaoDTO", Secao.class)
                .setParameter("secaoDTO", secaoDTO.numero())
                .getResultList()
                .isEmpty();

        // Verificar se o número da seção é menor ou igual a 0 ou se a seção já existe
        if (secaoDTO.numero() <= 0 || !secaoExistente) {
            throw new SecaoInvalidaException("O número da seção deve ser maior que zero e a seção não pode existir na base de dados.");
        }
        passToNext(secaoDTO);
    }
}
