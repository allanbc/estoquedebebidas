package com.magis5.estoquedebebidas.adapters.controllers;
import com.magis5.estoquedebebidas.adapters.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.adapters.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.application.services.TiposConsultaSecaoService;
import com.magis5.estoquedebebidas.application.services.SecaoService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/secoes")
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

    @PostMapping(value="/adicionarbebida/{secaoId}/{bebidaId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> adicionarBebida(@PathVariable("secaoId") Long secaoId, @PathVariable("bebidaId") Long bebidaId,
                                                @Valid @RequestBody MovimentoBebidasRequest request ) {
        secaoService.adicionarBebida(secaoId, bebidaId, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value="/removerbebida/{secaoId}/{bebidaId}", consumes = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping("/consultar-secoes-de-armazenamento")
    public ResponseEntity<List<Secao>> consultarSecoesDeArmazenamento(@RequestParam double volume, @RequestParam TipoBebida tipo) {
        return ResponseEntity.ok(tiposConsultaSecaoService.consultarSecoesDeArmazenamento(volume, tipo));
    }

    @GetMapping("/consultar-secoes-para-venda")
    public ResponseEntity<List<Secao>> consultarSecoesParaVendaDeBebidas(@RequestParam TipoBebida tipo) {
        return ResponseEntity.ok(tiposConsultaSecaoService.consultarSecoesParaVendaDeBebidas(tipo));
    }
}
