package com.magis5.estoquedebebidas.data.repositories;

import com.magis5.estoquedebebidas.domain.entities.Historico;
import com.magis5.estoquedebebidas.domain.repositories.HistoricoRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HistoricoRepositoryCustomImpl implements HistoricoRepositoryCustom {
    @PersistenceContext
    private EntityManager manager;

    /**
     * @param sortField Filtro pela data
     * @param sortDirection Ordena do primeiro para o último
     * @return retorna uma lista do histórico
     */
    public List<Historico> findHistorico(String sortField, String sortDirection) {
        String jpql = "SELECT h FROM Historico h ORDER BY h." + sortField + " " + sortDirection;
        TypedQuery<Historico> query = manager.createQuery(jpql, Historico.class);

        return query.getResultList();
    }
}
