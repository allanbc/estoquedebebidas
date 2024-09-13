package com.magis5.estoquedebebidas.api;

import com.magis5.estoquedebebidas.application.dto.BebidaDTO;
import com.magis5.estoquedebebidas.domain.model.Bebida;
import com.magis5.estoquedebebidas.domain.service.BebidaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/bebidas")
@RequiredArgsConstructor
public class BebidasController {

    private final BebidaService bebidaService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bebida> createBebidas(@RequestBody @Valid BebidaDTO bebidaDTO) {
        var cadBebida = bebidaService.criarBebida(bebidaDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cadBebida.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<Bebida>> getAllBebidas() {
        return ResponseEntity.ok(bebidaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bebida> getBebidasById(@PathVariable("id") Long id) {
        Bebida bebida = bebidaService.getByBebidaId(id);
        return ResponseEntity.ok(bebida);
    }

    @GetMapping("/{secaoId}/secoes")
    public ResponseEntity<List<Bebida>> listarBebidas(@PathVariable Long secaoId) {
        List<Bebida> bebidas = bebidaService.listarBebidasPorSecao(secaoId);
        return ResponseEntity.ok(bebidas);
    }

}
