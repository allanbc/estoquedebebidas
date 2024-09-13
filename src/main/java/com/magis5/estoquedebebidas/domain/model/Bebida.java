package com.magis5.estoquedebebidas.domain.model;

import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.util.ExistsId;
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
    @NotBlank(message = "O nome da bebida é obrigatório")
    private String nome;

    @Valid
    @NotNull(message = "A seção é obrigatória")
    @ExistsId(domainClass = Secao.class, fieldName = "id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Secao secao;

    @NotNull(message = "O tipo de bebida é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoBebida tipoBebida;

    public Bebida(Long id, String nome, TipoBebida tipoBebida) {
        this.id = id;
        this.nome = nome;
        this.tipoBebida = tipoBebida;
    }

    public Bebida(Long id, String nome, Secao secao, TipoBebida tipoBebida) {
        this.id = id;
        this.nome = nome;
        this.secao = secao;
        this.tipoBebida = tipoBebida;
    }

    public Bebida() {
    }

    public Bebida(String nome, Secao secao, String tipoBebida) {
        this.nome = nome;
        this.secao = secao;
        this.tipoBebida = Optional.of(tipoBebida).map(TipoBebida::valueOf).orElse(null);
    }
}


