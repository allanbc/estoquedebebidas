package com.magis5.estoquedebebidas.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Bebida bebida;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Secao secao;
    private double volume;
    @Enumerated(EnumType.STRING)
    private TipoMovimento tipoMovimento;
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT-3")
    private LocalDateTime dataHora;
    private String responsavel;

    public Historico() {
    }
}
