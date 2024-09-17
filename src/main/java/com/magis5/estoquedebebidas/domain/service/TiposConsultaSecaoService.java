package com.magis5.estoquedebebidas.domain.service;

import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TiposConsultaSecaoService {

    @PersistenceContext
    private final EntityManager manager;

    public TiposConsultaSecaoService(EntityManager manager) {
        this.manager = manager;
    }

    public double calcularVolumeTotalEstoque(TipoBebida tipo) {
        TypedQuery<Secao> query = manager.createQuery(
                "SELECT s FROM Secao s WHERE s.tipoBebida = :tipo", Secao.class);
        query.setParameter("tipo", tipo);

        List<Secao> secoes = query.getResultList();
        // Calcula o volume total usando Streams
        return secoes.stream()
                .mapToDouble(Secao::getVolumeAtual)
                .sum();
    }

    public List<Secao> consultarSecoesDeArmazenamento(double volume, TipoBebida tipo) {
        TypedQuery<Secao> query = manager.createQuery(
                "SELECT s FROM Secao s WHERE s.tipoBebida = :tipo", Secao.class);
        query.setParameter("tipo", tipo);

        List<Secao> secoes = query.getResultList();
        return secoes.stream()
                .filter(secao -> secao.getVolumeAtual() + volume <= secao.getCapacidadeMaxima())
                .collect(Collectors.toList());
    }

    public List<Secao> consultarSecoesParaVendaDeBebidas(TipoBebida tipo) {
        TypedQuery<Secao> query = manager.createQuery(
                "SELECT s FROM Secao s WHERE s.tipoBebida = :tipo", Secao.class);
        query.setParameter("tipo", tipo);

        List<Secao> secoes = query.getResultList();
        return secoes.stream()
                .filter(secao -> secao.getVolumeAtual() > 0)
                .collect(Collectors.toList());
    }
}
