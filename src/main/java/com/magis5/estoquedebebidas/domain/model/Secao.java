package com.magis5.estoquedebebidas.domain.model;

import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Builder
public class Secao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numSecao;
    @Enumerated(EnumType.STRING)
    private TipoBebida tipoDeBebida;
    private double capacidadeMaxima;
    private double volumeAtual;

    public Secao() {
    }

    public Secao(Long id, int numSecao, TipoBebida tipoDeBebida, double capacidadeMaxima, double volumeAtual) {
        this.id = id;
        this.numSecao = numSecao;
        this.tipoDeBebida = tipoDeBebida;
        this.capacidadeMaxima = capacidadeMaxima;
        this.volumeAtual = volumeAtual;
    }
    public static Secao secaoBuilder(Secao secao) {
        return Secao.builder()
                .id(secao.id)
                .numSecao(secao.numSecao)
                .tipoDeBebida(secao.tipoDeBebida)
                .capacidadeMaxima(secao.capacidadeMaxima)
                .volumeAtual(secao.volumeAtual)
                .build();
    }

    public void setVolumeAtual(double volumeAtual) {
        this.volumeAtual = volumeAtual;
    }

    public boolean podeArmazenar(String tipo, double volume) {
        return tipoDeBebida.name().equals(tipo)
                && volumeAtual + volume <= getCapacidadeMaxima();
    }
    public double getCapacidadeMaxima() {
        return tipoDeBebida.equals(TipoBebida.ALCOOLICA) ? 500.0 : 400.0;
    }
    public void validarAdicionarBebida(Bebida bebida, Secao secao, double volume) {
        if (!podeArmazenar(secao.tipoDeBebida.name(), volume)) {
            throw new IllegalArgumentException("Não é possível armazenar essa bebida na seção.");
        }
        // Atualiza o volume atual da seção
        this.volumeAtual += volume;
        System.out.println("Bebida " + bebida.getNome() + " adicionada à seção com volume: " + volume);
    }

//    public void adicionarVolume(double volume) {
//        if (volumeAtual + volume > capacidadeMaxima) {
//            throw new IllegalArgumentException("Capacidade máxima excedida");
//        }
//        this.volumeAtual += volume;
//    }
//    // Método para remover volume da seção
//    public void removerVolume(double volume) {
//        if (volume > volumeAtual) {
//            throw new IllegalArgumentException("Volume a ser removido é maior do que o volume atual");
//        }
//        this.volumeAtual -= volume;
//    }
//
//    // Verifica se a seção pode armazenar um determinado volume
//    public boolean podeArmazenar(double volume) {
//        return (volumeAtual + volume) <= capacidadeMaxima;
//    }
}


