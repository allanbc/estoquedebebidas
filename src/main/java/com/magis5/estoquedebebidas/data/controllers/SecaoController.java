package com.magis5.estoquedebebidas.data.controllers;
import com.magis5.estoquedebebidas.data.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.data.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.service.RegrasSecaoService;
import com.magis5.estoquedebebidas.domain.service.SecaoService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/secoes")
public class SecaoController {

    private final SecaoService secaoService;
    private final RegrasSecaoService regrasSecaoService;

    public SecaoController(SecaoService secaoService, RegrasSecaoService regrasSecaoService) {
        this.secaoService = secaoService;
        this.regrasSecaoService = regrasSecaoService;
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

    @PostMapping(value="/adicionarbebida/{secaoId}/{bebidaId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> adicionarBebida(@PathVariable("secaoId") Long secaoId, @PathVariable("bebidaId") Long bebidaId,
                                                @Valid @RequestBody MovimentoBebidasRequest request ) {
        secaoService.adicionarBebida(secaoId, bebidaId, request.getResponsavel(), request.getTipoMovimento(), request.getVolume());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value="/removerbebida/{secaoId}/{bebidaId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removerBebida(@PathVariable("secaoId") Long secaoId, @PathVariable("bebidaId") Long bebidaId,
                                                @Valid @RequestBody MovimentoBebidasRequest request ) {
        secaoService.retirarBebida(secaoId, bebidaId, request.getResponsavel(), request.getTipoMovimento(), request.getVolume());
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Secao> obterSecao(@PathVariable Long id) {
        return ResponseEntity.ok(secaoService.getBySecaoId(id));
    }

    @GetMapping("/volume-total-estoque")
    public ResponseEntity<Double> calcularVolumeTotalEstoque(@RequestParam TipoBebida tipoBebida) {
        return ResponseEntity.ok(regrasSecaoService.calcularVolumeTotalEstoque(tipoBebida));
    }

    @GetMapping("/consultar-secoes-de-armazenamento")
    public ResponseEntity<List<Secao>> consultarSecoesDeArmazenamento(@RequestParam double volume, @RequestParam TipoBebida tipo) {
        return ResponseEntity.ok(regrasSecaoService.consultarSecoesDeArmazenamento(volume, tipo));
    }

    @GetMapping("/consultar-secoes-para-venda")
    public ResponseEntity<List<Secao>> consultarSecoesParaVendaDeBebidas(@RequestParam TipoBebida tipo) {
        return ResponseEntity.ok(regrasSecaoService.consultarSecoesParaVendaDeBebidas(tipo));
    }
}
