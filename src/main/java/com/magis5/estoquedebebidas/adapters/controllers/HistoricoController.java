package com.magis5.estoquedebebidas.adapters.controllers;

import com.magis5.estoquedebebidas.domain.entities.Historico;
import com.magis5.estoquedebebidas.application.services.HistoricoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historico")
public class HistoricoController {
    private final HistoricoService historicoService;

    public HistoricoController(HistoricoService historicoService) {
        this.historicoService = historicoService;
    }

    @GetMapping("/consulta")
    public ResponseEntity<List<Historico>> consultaHistoricoOrderBySecaoDataAsc(
            @RequestParam(defaultValue = "dataHora") String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) Integer numSecao,
            @RequestParam(required = false) String tipoMovimento) {
        return ResponseEntity.ok(historicoService.consultaHistoricoOrderBySecaoDataAsc(sortField, sortDirection, numSecao, tipoMovimento));
    }
}
