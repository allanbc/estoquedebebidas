package com.magis5.estoquedebebidas.adapters.models;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;
@Builder
public record MovimentoBebidasRequest( String responsavel, TipoMovimento tipoMovimento, double volume) {
    public MovimentoBebidasRequest(String responsavel, TipoMovimento tipoMovimento, double volume) {
        this.responsavel = responsavel;
        this.tipoMovimento = tipoMovimento;
        this.volume = volume;
    }
}
