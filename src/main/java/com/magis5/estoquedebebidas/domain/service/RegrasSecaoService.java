package com.magis5.estoquedebebidas.domain.service;

import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.model.Secao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegrasSecaoService {

    @PersistenceContext
    private final EntityManager manager;

    public RegrasSecaoService(EntityManager manager) {
        this.manager = manager;
    }

    public double calcularVolumeTotalEstoque(String tipo) {
        TypedQuery<Secao> query = manager.createQuery(
                "SELECT b FROM Secao c WHERE c.tipoDeBebida = :tipo", Secao.class);
        query.setParameter("tipoDeBebida", tipo);

        List<Secao> secoes = query.getResultList();

        // Calcula o volume total usando Streams
        return secoes.stream()
                .mapToDouble(Secao::getVolumeAtual)
                .sum();
    }

    public List<Secao> consultarSecoesDeArmazenamento(double volume, TipoBebida tipo) {
        TypedQuery<Secao> query = manager.createQuery(
                "SELECT b FROM Bebida b WHERE b.tipo = :tipo", Secao.class);
        query.setParameter("tipo", tipo);

        List<Secao> secoes = query.getResultList();
        return secoes.stream()
                .filter(secao -> secao.getVolumeAtual() + volume <= secao.getCapacidadeMaxima())
                .collect(Collectors.toList());
    }

    public List<Secao> consultarSecoesParaVendaDeBebidas(TipoBebida tipo) {
        TypedQuery<Secao> query = manager.createQuery(
                "SELECT b FROM Bebida b WHERE b.tipo = :tipo", Secao.class);
        query.setParameter("tipo", tipo);

        List<Secao> secoes = query.getResultList();
        return secoes.stream()
                .filter(secao -> secao.getVolumeAtual() > 0)
                .collect(Collectors.toList());
    }
}
