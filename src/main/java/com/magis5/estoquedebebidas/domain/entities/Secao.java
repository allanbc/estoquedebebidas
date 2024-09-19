package com.magis5.estoquedebebidas.domain.entities;

import com.magis5.estoquedebebidas.core.exceptions.RemoverVolumeMaiorException;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
public class Secao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numSecao;
    @Enumerated(EnumType.STRING)
    private TipoBebida tipoBebida;
    private double capacidadeMaxima;
    private double volumeAtual;

    public Secao() {
    }

    public Secao(Long id, int numSecao, TipoBebida tipoBebida, double capacidadeMaxima, double volumeAtual) {
        this.id = id;
        this.numSecao = numSecao;
        this.tipoBebida = tipoBebida;
        this.capacidadeMaxima = capacidadeMaxima;
        this.volumeAtual = volumeAtual;
    }
    public static Secao secaoBuilder(Secao secao) {
        return Secao.builder()
                .id(secao.id)
                .numSecao(secao.numSecao)
                .tipoBebida(secao.tipoBebida)
                .capacidadeMaxima(secao.capacidadeMaxima)
                .volumeAtual(secao.volumeAtual)
                .build();
    }

    public void setVolumeAtual(double volumeAtual) {
        this.volumeAtual = volumeAtual;
    }

    public boolean armazenarVolume(String tipo, double volume) {
        return tipoBebida.name().equals(tipo)
                && volumeAtual + volume <= getCapacidadeMaxima();
    }

    public double getCapacidadeMaxima() {
        return tipoBebida.equals(TipoBebida.ALCOOLICA) ? 500.0 : 400.0;
    }

    public boolean removerVolumeMaiorQueZero(String tipo, double volume) {
        if(volumeAtual - volume <= 0 || volume <= 0) {
            throw new RemoverVolumeMaiorException(volume);
        } else {
            this.volumeAtual -= volume;
            return false;
        }
    }

    public boolean removerVolumeMaiorQueAtual(Bebida bebida, Secao secao, double volume) {
        if (volume > volumeAtual && volume > secao.getCapacidadeMaxima()) {
            throw new RemoverVolumeMaiorException(volume, bebida, secao);
        }
        return true;
    }
}


