package com.magis5.estoquedebebidas.adapters.controllers;

import com.magis5.estoquedebebidas.adapters.models.HistoricoDTO;
import com.magis5.estoquedebebidas.domain.entities.Historico;
import com.magis5.estoquedebebidas.application.services.HistoricoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historico")
@Tag(name = "Histórico", description = "Consulta de histórico de movimentações")
public class HistoricoController {
    private final HistoricoService historicoService;

    public HistoricoController(HistoricoService historicoService) {
        this.historicoService = historicoService;
    }

    @Operation(
            operationId = "listarHistorico",
            summary = "Lista histórico",
            description = "Retorna eventos do histórico."
    )
    @ApiResponse(responseCode = "200", description = "Lista de eventos",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = HistoricoDTO.class))))
    @GetMapping
    public ResponseEntity<List<Historico>> consultaHistoricoOrderBySecaoDataAsc(
            @RequestParam(defaultValue = "dataHora") String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) Integer numSecao,
            @RequestParam(required = false) String tipoMovimento) {
        return ResponseEntity.ok(historicoService.consultaHistoricoOrderBySecaoDataAsc(sortField, sortDirection, numSecao, tipoMovimento));
    }
}
