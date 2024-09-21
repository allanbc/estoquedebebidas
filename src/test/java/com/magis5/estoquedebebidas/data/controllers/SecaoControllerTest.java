package com.magis5.estoquedebebidas.data.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magis5.estoquedebebidas.data.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.data.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.service.BebidaService;
import com.magis5.estoquedebebidas.domain.service.SecaoService;
import com.magis5.estoquedebebidas.domain.service.TiposConsultaSecaoService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class SecaoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SecaoService secaoService;

    @MockBean
    private BebidaService bebidaService;

    @MockBean
    private TiposConsultaSecaoService tiposConsultaSecaoService;

    @Autowired
    private EntityManager manager;

    private ObjectMapper objectMapper;
    @Captor
    private ArgumentCaptor<SecaoDTO> secaoDTOArgumentCaptor;

    private SecaoController secaoController;


    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        // Simula a busca por ID
        when(secaoService.getBySecaoId(1L)).thenReturn(
                new Secao(1L, 1, TipoBebida.ALCOOLICA, 300.0, 20.0)
        );

        secaoController = new SecaoController(secaoService, tiposConsultaSecaoService);
    }


    @Test
    @DisplayName("Test Create Secao")
    void testCriaSecaoERetorna201() throws Exception {

        var secaoDTO = new SecaoDTO(1, TipoBebida.ALCOOLICA, 300.0, 20.0);

        String payload = objectMapper.writeValueAsString(secaoDTO);

        var secao = new Secao(1L, 1, TipoBebida.ALCOOLICA, 300.0, 20.0);
        // Mockar o retorno do serviço
        when(secaoService.criarSecao(any(SecaoDTO.class))).thenReturn(Secao.secaoBuilder(secao));

        // Act
        var response = mockMvc.perform(
                        post("/api/secoes")
                                .content(payload)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()
                ).andReturn().getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @ParameterizedTest
    @MethodSource("provideRespostasEntradaSaida")
    @DisplayName("Test adiciona bebida na Seção pelo ID da bebida e pelo ID da seção")
    void testAdicionarBebidaERetornar200Ou404(String movimento, Long secaoId, Long bebidaId, int expectedStatus) throws Exception {
        // Arrange
        String url;
        MovimentoBebidasRequest request;
        if(movimento.equalsIgnoreCase("ENTRADA")) {
            url = "/api/secoes/adicionarbebida/{secaoId}/{bebidaId}";
            request = MovimentoBebidasRequest.builder()
                    .responsavel("Allan")
                    .tipoMovimento(TipoMovimento.ENTRADA)
                    .volume(30.0)
                    .build();
        } else {
            url = "/api/secoes/removerbebida/{secaoId}/{bebidaId}";
            request = MovimentoBebidasRequest.builder()
                    .responsavel("Allan")
                    .tipoMovimento(TipoMovimento.SAIDA)
                    .volume(30.0)
                    .build();
        }

        if (secaoId != null && secaoId > 0) {
            Secao secaoMock = mock(Secao.class);
            when(secaoService.getBySecaoId(secaoId)).thenReturn(secaoMock);
        } else {
            when(secaoService.getBySecaoId(secaoId)).thenThrow(new NoSuchElementException());
        }

        if (bebidaId != null && bebidaId > 0) {
            Bebida bebidaMock = mock(Bebida.class);
            when(bebidaService.getByBebidaId(bebidaId)).thenReturn(bebidaMock);
        } else {
            when(bebidaService.getByBebidaId(bebidaId)).thenThrow(new NoSuchElementException());
        }

        // Act
        var response = mockMvc.perform(
                post(url, secaoId, bebidaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andReturn().getResponse();

        // Assert
        assertEquals(expectedStatus, response.getStatus());
    }

    @ParameterizedTest
    @MethodSource("provideRespostas")
    @DisplayName("Test retorna Seção pelo ID")
    void deveRetornarStatusCode200Ou404ParaBuscarSecaoPorId(Long secaoId, int expectedStatus) throws Exception {
        // Arrange
        if (secaoId != null && secaoId > 0) {
            Secao secaoMock = mock(Secao.class);
            when(secaoService.getBySecaoId(secaoId)).thenReturn(secaoMock);
        } else {
                when(secaoService.getBySecaoId(secaoId)).thenThrow(new NoSuchElementException());
            }

        // Act
        var response = mockMvc.perform(
                get("/api/secoes/{id}", secaoId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        assertEquals(expectedStatus, response.getStatus());
    }

    @ParameterizedTest
    @MethodSource("provideRespostasTipoBebida")
    @DisplayName("Test calcular volume total")
    void testCalcularVolumeTotalEstoque(TipoBebida tipoBebida) throws Exception {
        // Arrange
        double volumeTotal = 150.0;
        double capacidade = 500.0;

        var secaoMock = Secao.builder()
                .tipoBebida(tipoBebida)
                .numSecao(1)
                .capacidadeMaxima(capacidade)
                .volumeAtual(200).build();


        when(tiposConsultaSecaoService.calcularVolumeTotalEstoque(tipoBebida)).thenReturn(volumeTotal);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/secoes/volume-total-estoque")
                        .param("tipoBebida", tipoBebida.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(String.valueOf(volumeTotal)));

    }

    @Test
    @DisplayName("Test consulta seções de armazenamento com parâmetros válidos")
    void testConsultarSecoesDeArmazenamento() throws Exception {
        // Arrange
        double volume = 100.0;
        double capacidade = 500.0;
        TipoBebida tipo = TipoBebida.ALCOOLICA;
        var secaoMock = Secao.builder()
            .tipoBebida(TipoBebida.ALCOOLICA)
                    .numSecao(1)
                            .capacidadeMaxima(capacidade)
                                    .volumeAtual(200).build();

        List<Secao> secoes = Collections.singletonList(secaoMock);

        when(tiposConsultaSecaoService.consultarSecoesDeArmazenamento(volume, tipo)).thenReturn(secoes);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/secoes/consultar-secoes-de-armazenamento")
                        .param("volume", String.valueOf(volume))
                        .param("tipo", tipo.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists());
    }

    @Test
    @DisplayName("Test consulta seções para venda de bebidas com parâmetros válidos")
    void testConsultarSecoesParaVendaDeBebidas() throws Exception {
        // Arrange
        double capacidade = 500.0;
        TipoBebida tipo = TipoBebida.ALCOOLICA;
        var secaoMock = Secao.builder()
                .tipoBebida(TipoBebida.ALCOOLICA)
                .numSecao(1)
                .capacidadeMaxima(capacidade)
                .volumeAtual(200).build();

        List<Secao> secoes = Collections.singletonList(secaoMock);

        when(tiposConsultaSecaoService.consultarSecoesParaVendaDeBebidas(tipo)).thenReturn(secoes);

        // Act
        mockMvc.perform(MockMvcRequestBuilders.get("/api/secoes/consultar-secoes-para-venda")
                        .param("tipo", tipo.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").exists());
    }

    @Test
    void consultarSecoesParaVendaDeBebidas() {
    }

    private static Stream<Arguments> provideRespostas() {
        return Stream.of(
                Arguments.of(1L, HttpStatus.OK.value()),
                Arguments.of(null, HttpStatus.NOT_FOUND.value()),
                Arguments.of(0L, HttpStatus.NOT_FOUND.value())
        );
    }

    private static Stream<Arguments> provideRespostasEntradaSaida() {
        String entrada = "ENTRADA";
        String saida = "SAIDA";
        return Stream.of(
                Arguments.of(entrada, 1L, 1L, HttpStatus.NO_CONTENT.value()),
                Arguments.of(entrada, null, null, HttpStatus.NOT_FOUND.value()),
                Arguments.of(entrada, 0L, null, HttpStatus.NOT_FOUND.value()),

                Arguments.of(saida, 1L, 1L, HttpStatus.NO_CONTENT.value()),
                Arguments.of(saida, null, null, HttpStatus.NOT_FOUND.value()),
                Arguments.of(saida, 0L, null, HttpStatus.NOT_FOUND.value())
        );
    }

    private static Stream<Arguments> provideRespostasTipoBebida() {
        return Stream.of(
                Arguments.of(TipoBebida.ALCOOLICA),
                Arguments.of(TipoBebida.NAO_ALCOOLICA)
        );
    }

}