package com.magis5.estoquedebebidas.adapters.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magis5.estoquedebebidas.adapters.models.BebidaDTO;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

public class JsonBebidasArgumentsProvider implements ArgumentsProvider {

    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws JsonProcessingException {
        var bebida1 = new BebidaDTO(null, null, null);
        var bebida2 = new BebidaDTO("Cerveja",TipoBebida.ALCOOLICA.name(), 1L);
        var bebida3 = new BebidaDTO("",TipoBebida.ALCOOLICA.name(), 1L);

        return Stream.of(
                Arguments.of(bebida1, HttpStatus.BAD_REQUEST.value()),
                Arguments.of(bebida2, HttpStatus.CREATED.value()),
                Arguments.of(bebida3, HttpStatus.BAD_REQUEST.value())
        );
    }
}
