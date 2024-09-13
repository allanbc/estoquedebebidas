package com.magis5.estoquedebebidas.domain.service;

import com.magis5.estoquedebebidas.application.dto.BebidaDTO;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.exception.BebidaNotFoundException;
import com.magis5.estoquedebebidas.domain.model.Bebida;
import com.magis5.estoquedebebidas.domain.model.Secao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@RequiredArgsConstructor
public class BebidaService {
    private static final Logger LOG = getLogger(BebidaService.class);

    @PersistenceContext
    private final EntityManager manager;

    public Bebida converterDtoToEntity(BebidaDTO bebidaDTO) {
        return Bebida.builder()
                .nome(bebidaDTO.nome())
                .id(bebidaDTO.secaoId())
                .tipoBebida(Optional.of(bebidaDTO.tipoBebida()).map(TipoBebida::valueOf).orElse(null))
                .build();
    }

    @Transactional
    public Bebida criarBebida(BebidaDTO bebidaDTO) {

        LOG.info("Cadastrando uma bebida: {}", bebidaDTO);

        Bebida bebida = converterDtoToEntity(bebidaDTO);
        bebida = bebidaDTO.toDtoBebida(
                id -> manager.find(Secao.class, bebidaDTO.secaoId())
        );
        manager.persist(bebida);
        return bebida;
    }

    public List<Bebida> findAll() {
        return manager
                .createQuery("SELECT b FROM Bebida b", Bebida.class)
                .getResultList();
    }

    public Bebida getByBebidaId(Long bebidaId) {
        LOG.info("Buscando de seção pelo id {}", bebidaId);
        return Optional.ofNullable(manager.find(Bebida.class, bebidaId))
                .orElseThrow(() -> new BebidaNotFoundException(bebidaId));
    }

    public List<Bebida> listarBebidasPorSecao(Long secaoId) {
        return manager
                .createQuery("SELECT b FROM Bebida b WHERE b.secao.id = :secaoId", Bebida.class)
                .setParameter("secaoId", secaoId)
                .getResultList();
    }
}
