package com.magis5.estoquedebebidas.domain.repository;

import com.magis5.estoquedebebidas.domain.model.Secao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecaoRepository extends JpaRepository<Secao, Long> {
}
