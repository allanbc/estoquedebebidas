package com.magis5.estoquedebebidas.adapters.models;

import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.application.usecases.validators.interfaces.UniqueValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.Assert;

import java.util.function.Function;

public record BebidaDTO(
        @NotBlank(message = "O nome da bebida é obrigatório")
        @UniqueValue(domainClass = Bebida.class, fieldName = "nome", message = "Existe uma bebida cadastrada com esse nome!")
        @Schema(description = "Nome da bebida", example = "IPA Hopzilla", requiredMode = Schema.RequiredMode.REQUIRED)
        String nome,
        @NotNull(message = "O tipo de bebida é obrigatório")
        @Schema(description = "Tipo de Bebida", example = "ALLCOOLICA", requiredMode = Schema.RequiredMode.REQUIRED)
        String tipoBebida,
        @Valid
        @Schema(description = "ID da seção", example = "1")
        Long secaoId
) {
//    public BebidaDTO(BebidaDTO bebidaDTO) {
//        this(bebidaDTO.nome(), bebidaDTO.tipoBebida(), bebidaDTO.secaoId);
//    }
    public Bebida toDtoBebida(Function<Long, Secao> buscaSecao) {
        Secao secao = buscaSecao.apply(secaoId);
        Assert.notNull(secao, String.format("Você esta querendo cadastrar uma bebida para uma seção que nao existe no banco: secaoId: %s ", secaoId));
        return new Bebida(nome, secao, tipoBebida);
    }

    public boolean validaPayload() {
            return nome != null && tipoBebida != null && secaoId != null;
    }
}
