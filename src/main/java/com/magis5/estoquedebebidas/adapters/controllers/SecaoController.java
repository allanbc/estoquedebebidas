package com.magis5.estoquedebebidas.adapters.controllers;
import com.magis5.estoquedebebidas.adapters.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.adapters.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.application.services.TiposConsultaSecaoService;
import com.magis5.estoquedebebidas.application.services.SecaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/secoes")
@Tag(name = "Seções", description = "Operações com seções e suas bebidas")
public class SecaoController {

    private final SecaoService secaoService;
    private final TiposConsultaSecaoService tiposConsultaSecaoService;

    public SecaoController(SecaoService secaoService, TiposConsultaSecaoService tiposConsultaSecaoService) {
        this.secaoService = secaoService;
        this.tiposConsultaSecaoService = tiposConsultaSecaoService;
    }

    @Operation(
            operationId = "adicionarSecaoPost",
            summary = "Adiciona uma seção",
            description = "Cria uma seção {secaoId}. "
                    + "Cria o recurso Seção no catálogo e retorna 201 se criado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Seção criada",
                    headers = @io.swagger.v3.oas.annotations.headers.Header(
                            name = "Location", description = "URI do vínculo criado",
                            schema = @Schema(type = "string", format = "uri")
                    )),
            @ApiResponse(responseCode = "204", description = "Associação já existia (idempotente)"),
            @ApiResponse(responseCode = "404", description = "Seção ou bebida não encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflito (regra de negócio)")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Secao> create(@Valid @RequestBody SecaoDTO secaoDTO) {
        // Constrói a URI do recurso recém-criado
        var novasecao = secaoService.criarSecao(secaoDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novasecao.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(
            operationId = "listarSecoes",
            summary = "Lista seções",
            description = "Retorna todas as seções cadastradas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de seções",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SecaoDTO.class))))
    })
    @GetMapping
    public ResponseEntity<List<Secao>> getAllSecoes() {
        return ResponseEntity.ok(secaoService.findAll());
    }

    @Operation(
            operationId = "adicionarBebidaSecaoPost",
            summary = "Adiciona bebida a uma seção",
            description = "Cria o vínculo entre a seção {secaoId} e a bebida {bebidaId}. "
                    + "Não cria o recurso Bebida no catálogo. Idempotente: retorna 201 se criado, 204 se já existia.",
            parameters = {
                    @Parameter(name = "secaoId", in = ParameterIn.PATH, required = true, description = "ID da seção", example = "10")

            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Associação criada",
                    headers = @io.swagger.v3.oas.annotations.headers.Header(
                            name = "Location", description = "URI do vínculo criado",
                            schema = @Schema(type = "string", format = "uri")
                    )),
            @ApiResponse(responseCode = "204", description = "Associação já existia (idempotente)"),
            @ApiResponse(responseCode = "404", description = "Seção ou bebida não encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflito (regra de negócio)")
    })
    @PostMapping(value="/{secaoId}/{bebidaId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> adicionarBebida(@PathVariable("secaoId") Long secaoId, @PathVariable("bebidaId") Long bebidaId,
                                                @Valid @RequestBody MovimentoBebidasRequest request ) {
        secaoService.adicionarBebida(secaoId, bebidaId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Remove bebida de uma seção",
            description = "Remove o vínculo entre a seção {secaoId} e a bebida {bebidaId}. "
                    + "Não exclui o recurso Bebida. Operação idempotente.",
            parameters = {
                    @Parameter(name = "secaoId", in = ParameterIn.PATH, required = true, description = "ID da seção", example = "10"),
                    @Parameter(name = "bebidaId", in = ParameterIn.PATH, required = true, description = "ID da bebida", example = "42"),
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Associação removida (ou já inexistente)"),
            @ApiResponse(responseCode = "404", description = "Seção ou bebida não encontrada"),
            @ApiResponse(responseCode = "409", description = "Regra de negócio impede a remoção")
    })
    @DeleteMapping(value="/{secaoId}/{bebidaId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removerBebida(@PathVariable("secaoId") Long secaoId, @PathVariable("bebidaId") Long bebidaId,
                                                @Valid @RequestBody MovimentoBebidasRequest request ) {
        secaoService.retirarBebida(secaoId, bebidaId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            operationId = "obterSecaoPorId",
            summary = "Obtém seção por ID",
            description = "Retorna os detalhes da seção informada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Seção encontrada",
                    content = @Content(schema = @Schema(implementation = SecaoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Seção não encontrada")
    })
    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Secao> getSecaoById(
            @Parameter(name = "id", in = ParameterIn.PATH, required = true, description = "ID da seção", example = "10")
            @PathVariable Long id
            ) {
        return ResponseEntity.ok(secaoService.getBySecaoId(id));
    }

    @Operation(
            operationId = "volumeTotalEstoque",
            summary = "Volume total em estoque",
            description = "Retorna o volume total de bebidas em estoque (somatório em litros, por exemplo)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Volume total",
                    content = @Content(schema = @Schema(implementation = Double.class)))
    })
    @GetMapping("/volume-total-estoque")
    public ResponseEntity<Double> calcularVolumeTotalEstoque(@RequestParam TipoBebida tipoBebida) {
        return ResponseEntity.ok(tiposConsultaSecaoService.calcularVolumeTotalEstoque(tipoBebida));
    }

    @Operation(
            operationId = "secoesParaVenda",
            summary = "Seções para venda",
            description = "Lista seções marcadas como aptas para venda."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de seções",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SecaoDTO.class))))
    })
    @GetMapping("/secoes-de-armazenamento")
    public ResponseEntity<List<Secao>> consultarSecoesDeArmazenamento(@RequestParam double volume, @RequestParam TipoBebida tipo) {
        return ResponseEntity.ok(tiposConsultaSecaoService.consultarSecoesDeArmazenamento(volume, tipo));
    }

    @Operation(
            operationId = "secoesDeArmazenamento",
            summary = "Seções de armazenamento",
            description = "Lista seções destinadas a armazenamento."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de seções",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SecaoDTO.class))))
    })
    @GetMapping("/secoes-para-venda")
    public ResponseEntity<List<Secao>> consultarSecoesParaVendaDeBebidas(@RequestParam TipoBebida tipo) {
        return ResponseEntity.ok(tiposConsultaSecaoService.consultarSecoesParaVendaDeBebidas(tipo));
    }
}
