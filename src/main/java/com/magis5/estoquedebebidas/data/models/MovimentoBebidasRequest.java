package com.magis5.estoquedebebidas.data.models;

import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
public class MovimentoBebidasRequest {
    private String responsavel;
    private TipoMovimento tipoMovimento;
    private double volume;
}
