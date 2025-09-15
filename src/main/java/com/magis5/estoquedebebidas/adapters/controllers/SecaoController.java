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

    @GetMapping
    public ResponseEntity<List<Secao>> getAllSecoes() {
        return ResponseEntity.ok(secaoService.findAll());
    }

    @Operation(
            operationId = "adicionarBebidaSecaoPut",
            summary = "Adiciona bebida a uma seção",
            description = "Cria o vínculo entre a seção {secaoId} e a bebida {bebidaId}. "
                    + "Não cria o recurso Bebida no catálogo. Idempotente: retorna 201 se criado, 204 se já existia.",
            parameters = {
                    @Parameter(name = "secaoId", in = ParameterIn.PATH, required = true, description = "ID da seção", example = "10"),
                    @Parameter(name = "bebidaId", in = ParameterIn.PATH, required = true, description = "ID da bebida", example = "42"),
                    @Parameter(name = "If-Match", in = ParameterIn.HEADER, required = false,
                            description = "ETag da seção para controle de concorrência (412 se não bater)"),
                    @Parameter(name = "X-Correlation-Id", in = ParameterIn.HEADER, required = false,
                            description = "ID de correlação para rastreabilidade")
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
            @ApiResponse(responseCode = "409", description = "Conflito (regra de negócio)"),
            @ApiResponse(responseCode = "412", description = "Falha de pré-condição (If-Match/ETag)"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão")
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
                    @Parameter(name = "If-Match", in = ParameterIn.HEADER, required = false,
                            description = "ETag da seção para controle de concorrência (retorna 412 se não bater)"),
                    @Parameter(name = "X-Correlation-Id", in = ParameterIn.HEADER, required = false,
                            description = "ID de correlação para rastreabilidade")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Associação removida (ou já inexistente)"),
            @ApiResponse(responseCode = "404", description = "Seção ou bebida não encontrada"),
            @ApiResponse(responseCode = "409", description = "Regra de negócio impede a remoção"),
            @ApiResponse(responseCode = "412", description = "Falha de pré-condição (If-Match/ETag)"),
            @ApiResponse(responseCode = "401", description = "Não autenticado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão")
    })
    @DeleteMapping(value="/{secaoId}/{bebidaId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removerBebida(@PathVariable("secaoId") Long secaoId, @PathVariable("bebidaId") Long bebidaId,
                                                @Valid @RequestBody MovimentoBebidasRequest request ) {
        secaoService.retirarBebida(secaoId, bebidaId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Secao> getSecaoById(@PathVariable Long id) {
        return ResponseEntity.ok(secaoService.getBySecaoId(id));
    }

    @GetMapping("/volume-total-estoque")
    public ResponseEntity<Double> calcularVolumeTotalEstoque(@RequestParam TipoBebida tipoBebida) {
        return ResponseEntity.ok(tiposConsultaSecaoService.calcularVolumeTotalEstoque(tipoBebida));
    }

    @GetMapping("/secoes-de-armazenamento")
    public ResponseEntity<List<Secao>> consultarSecoesDeArmazenamento(@RequestParam double volume, @RequestParam TipoBebida tipo) {
        return ResponseEntity.ok(tiposConsultaSecaoService.consultarSecoesDeArmazenamento(volume, tipo));
    }

    @GetMapping("/secoes-para-venda")
    public ResponseEntity<List<Secao>> consultarSecoesParaVendaDeBebidas(@RequestParam TipoBebida tipo) {
        return ResponseEntity.ok(tiposConsultaSecaoService.consultarSecoesParaVendaDeBebidas(tipo));
    }
}
