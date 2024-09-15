package com.magis5.estoquedebebidas.domain.entities;

import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
public class Bebida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Secao secao;

    @Enumerated(EnumType.STRING)
    private TipoBebida tipoBebida;

    public Bebida(Long id, @Valid @NotNull Secao secao, @NotNull TipoBebida tipoBebida) {
        this.id = id;
        this.nome = nome;
        this.tipoBebida = tipoBebida;
    }

    public Bebida(Long id, @NotBlank String nome, @Valid @NotNull Secao secao, @NotNull TipoBebida tipoBebida) {
        this.id = id;
        this.nome = nome;
        this.secao = secao;
        this.tipoBebida = tipoBebida;
    }

    public Bebida() {
    }

    public Bebida(@NotBlank String nome, @Valid @NotNull Secao secao, @NotNull String tipoBebida) {
        this.nome = nome;
        this.secao = secao;
        this.tipoBebida = Optional.of(tipoBebida).map(TipoBebida::valueOf).orElse(null);
    }


}


