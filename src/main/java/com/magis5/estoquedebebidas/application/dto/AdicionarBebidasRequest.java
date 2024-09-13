package com.magis5.estoquedebebidas.application.dto;

import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import lombok.Getter;

@Getter
public class AdicionarBebidasRequest {
    private String responsavel;
    private TipoMovimento tipoMovimento;
    private double volume;
}
