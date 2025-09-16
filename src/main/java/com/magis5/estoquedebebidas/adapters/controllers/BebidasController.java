package com.magis5.estoquedebebidas.adapters.controllers;

import com.magis5.estoquedebebidas.adapters.models.BebidaDTO;
import com.magis5.estoquedebebidas.adapters.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.application.services.BebidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bebidas")
@RequiredArgsConstructor
@Tag(name = "Bebidas", description = "Operações com bebidas")
public class BebidasController {

    private final BebidaService bebidaService;

    @Operation(
            operationId = "criarBebida",
            summary = "Cria uma bebida",
            description = "Cria o recurso Bebida no catálogo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bebida criada",
                    headers = @Header(name = "Location", description = "URI do recurso criado",
                            schema = @Schema(type = "string", format = "uri")),
                    content = @Content(schema = @Schema(implementation = BebidaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "409", description = "Conflito (regra de negócio)"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bebida> createBebidas(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados para criação da bebida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BebidaDTO.class)
                    )
            )
            @Valid @RequestBody BebidaDTO bebidaDTO) throws BadRequestException {
        var cadBebida = bebidaService.criarBebida(bebidaDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cadBebida.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(
            operationId = "listarBebidas",
            summary = "Lista bebidas",
            description = "Retorna todas as bebidas cadastradas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de bebidas",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BebidaDTO.class))))
    })
    @GetMapping
    public ResponseEntity<List<Bebida>> getAllBebidas() {
        return ResponseEntity.ok(bebidaService.findAll());
    }

    @Operation(
            operationId = "obterBebidaPorId",
            summary = "Obtém bebida por ID",
            description = "Retorna os detalhes da bebida informada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Bebida encontrada",
                    content = @Content(schema = @Schema(implementation = BebidaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Bebida não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Bebida> getBebidasById(
            @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "ID da bebida", example = "42")
            @PathVariable("id") Long id) {
        Bebida bebida = bebidaService.getByBebidaId(id);
        return ResponseEntity.ok(bebida);
    }

    @Operation(
            operationId = "listarSecoesPorIdentificadorInformado",
            summary = "Lista seções relacionadas",
            description = "Retorna as seções relacionadas ao identificador informado no caminho."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de seções",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SecaoDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado")
    })
    @GetMapping("/{secaoId}/secoes")
    public ResponseEntity<List<Bebida>> listarBebidas(
            @Parameter(name = "secaoId", in = ParameterIn.PATH, required = true, description = "Identificador usado na rota", example = "10")
            @PathVariable Long secaoId) {
        List<Bebida> bebidas = bebidaService.listarBebidasPorSecao(secaoId);
        return ResponseEntity.ok(bebidas);
    }

}
