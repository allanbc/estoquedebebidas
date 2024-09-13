package com.magis5.estoquedebebidas.domain.repository;

import com.magis5.estoquedebebidas.domain.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Long> {
}
