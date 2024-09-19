package com.magis5.estoquedebebidas.data.repositories;

import com.magis5.estoquedebebidas.data.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Historico;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.repositories.HistoricoRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @Override
    public boolean verificarSePodeCadastrarBebidaSecao(String tipoBebida, Long secaoId) {
        // Obter a data do início e fim do dia atual
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime fimDia = inicioDia.plus(1, ChronoUnit.DAYS);

        // Query para verificar se houve entrada de bebida alcoólica na seção no dia atual
        String jpql = "SELECT COUNT(h) FROM Historico h " +
                "JOIN h.bebida b " +
                "WHERE h.secao.id = :secaoId " +
                "AND b.tipoBebida = 'ALCOOLICA' " +
                "AND h.dataHora BETWEEN :inicioDia AND :fimDia";

        TypedQuery<Long> query = manager.createQuery(jpql, Long.class);
        query.setParameter("secaoId", secaoId);
        query.setParameter("inicioDia", inicioDia);
        query.setParameter("fimDia", fimDia);

        Long count = query.getSingleResult();

        // Retorna true se não houver registros de ALCOOLICA no mesmo dia, permitindo a inserção de NAO ALCOOLICA
        return count == 0;
    }

    @Override
    public void atualizarHistorico(Secao pesquisaSecao, Secao secao, Bebida bebida, MovimentoBebidasRequest request) {
        // Adiciona ao histórico
        Historico historico = Historico.builder()
                .secao(pesquisaSecao)
                .bebida(bebida)
                .volume(request.volume())
                .tipoMovimento(request.tipoMovimento())
                .responsavel(request.responsavel())
                .build();

        manager.persist(historico);
    }
}
