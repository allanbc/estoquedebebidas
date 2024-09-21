package com.magis5.estoquedebebidas.data.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magis5.estoquedebebidas.data.models.BebidaDTO;
import com.magis5.estoquedebebidas.data.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.service.BebidaService;
import com.magis5.estoquedebebidas.domain.service.SecaoService;
import com.magis5.estoquedebebidas.domain.validators.implementations.ExistsIdValueValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("tests")
class BebidasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecaoService secaoService;

    @MockBean
    private BebidaService bebidaService;

    private  ObjectMapper objectMapper;

    @MockBean
    private ExistsIdValueValidator existsIdValueValidator;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();

        when(secaoService.criarSecao(any(SecaoDTO.class)))
                .thenAnswer(invocation -> {
                    SecaoDTO dto = invocation.getArgument(0);
                    return Secao.builder()
                            .id(1L)
                            .numSecao(dto.numero())
                            .tipoBebida(dto.tipoBebida())
                            .capacidadeMaxima(dto.capacidadeMaxima())
                            .volumeAtual(dto.volume())
                            .build();
                });
        // Simula a busca por ID
        when(secaoService.getBySecaoId(1L)).thenReturn(
                new Secao(1L, 1, TipoBebida.ALCOOLICA, 300.0, 20.0)
        );

    }

    @Order(1)
    @Test
    @DisplayName("Test Create Secao")
    void testCreateSecaoERetorna201() throws Exception {

        var secao = new SecaoDTO(1, TipoBebida.ALCOOLICA, 300.0, 20.0);

        String payload = objectMapper.writeValueAsString(secao);
        // Act
        var response = mockMvc.perform(
                        post("/api/secoes")
                                .content(payload)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()
                ).andReturn().getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Order(2)
    @ParameterizedTest
    @ArgumentsSource(JsonBebidasArgumentsProvider.class)
    @DisplayName("Test Create Bebida Integrada - Valid Payload")
    void deveCriarBebidaComSecaoComSucessoERetornarStatus201Ou400(BebidaDTO payloadBebida, int expectedStatus) throws Exception {
        //Arranje
        String payload = objectMapper.writeValueAsString(payloadBebida);
        var secaoDTO = new SecaoDTO(1, TipoBebida.ALCOOLICA, 300.0, 20.0);
        var secao = Secao.builder()
                .id(1L)
                .numSecao(secaoDTO.numero())
                .tipoBebida(secaoDTO.tipoBebida())
                .capacidadeMaxima(secaoDTO.capacidadeMaxima())
                .volumeAtual(secaoDTO.volume())
                .build();
        // Mockar o retorno do serviço
        Bebida bebidaMock = new Bebida(1L, "Cerveja", secao, TipoBebida.ALCOOLICA);
        when(bebidaService.criarBebida(any(BebidaDTO.class))).thenReturn(bebidaMock);

        // Act
        var response = mockMvc.perform(
                        post("/api/bebidas")
                                .content(payload)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()
                ).andReturn().getResponse();

        // Assert
        assertEquals(expectedStatus, response.getStatus());
    }

    @ParameterizedTest
    @ArgumentsSource(JsonBebidasArgumentsProvider.class)
    @DisplayName("Test Create Bebida - Valid Payload")
    void deveCriarBebidaComSucessoERetornarStatus201Ou400(BebidaDTO payloadBebida, int expectedStatus) throws Exception {
        //Arranje
        String payload = objectMapper.writeValueAsString(payloadBebida);

        // Mockar o retorno do serviço
        Secao secaoMock = mock(Secao.class);
        when(secaoMock.getId()).thenReturn(1L); // Mockando o ID da secao

        Bebida bebidaMock = new Bebida(1L, "Cerveja", secaoMock, TipoBebida.ALCOOLICA);
        when(bebidaService.criarBebida(any(BebidaDTO.class))).thenReturn(bebidaMock);

        // Act
        var response = mockMvc.perform(
                        post("/api/bebidas")
                                .content(payload)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()
                ).andReturn().getResponse();

        // Assert
        assertEquals(expectedStatus, response.getStatus());
    }

    @ParameterizedTest
    @DisplayName("Test Create Bebida Parameterized")
    @MethodSource("provideValidBebidaDTO")
    void deveCriarBebidaComSucesso(BebidaDTO bebidaDTO, int expectedStatus) throws Exception {
        // Arrange
        Secao secaoMock = mock(Secao.class);
        when(secaoMock.getId()).thenReturn(1L); //

        Bebida bebidaMock = mock(Bebida.class);
        when(bebidaMock.getId()).thenReturn(1L);

        // Mockando o serviço para buscar a seção
        when(secaoService.getBySecaoId(1L)).thenReturn(secaoMock);

        when(bebidaService.criarBebida(any(BebidaDTO.class))).thenReturn(bebidaMock);

        // Act
        var response = mockMvc.perform(
                post("/api/bebidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bebidaDTO))
        ).andReturn().getResponse();

        // Assert
        assertEquals(expectedStatus, response.getStatus());
    }

    @ParameterizedTest
    @MethodSource("provideRespostas")
    @DisplayName("Test retorna Bebida pelo ID")
    void deveRetornarStatusCode200Ou404ParaBuscarBebidaPorId(Long id, int expectedStatus) throws Exception {
        // Arrange
        if (id != null && id > 0) {
            Bebida bebidaMock = mock(Bebida.class);
            when(bebidaService.getByBebidaId(id)).thenReturn(bebidaMock);
        } else {
            when(bebidaService.getByBebidaId(id)).thenThrow(new NoSuchElementException());
        }

        // Act
        var response = mockMvc.perform(
                get("/api/bebidas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        assertEquals(expectedStatus, response.getStatus());
    }

    @Test
    @DisplayName("Test retorna Bebida pela Seção")
    void deveRetornarStatusCode200ParaListarBebidasPorSecao() throws Exception {
        // Arrange
        Long secaoId = 1L;
        List<Bebida> bebidas = Arrays.asList(
                mock(Bebida.class),
                mock(Bebida.class)
        );
        when(bebidaService.listarBebidasPorSecao(secaoId)).thenReturn(bebidas);

        // Act
        var response = mockMvc.perform(
                get("/api/bebidas/{secaoId}/secoes", secaoId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @DisplayName("Test retorna todas as Bebidas")
    void deveRetornarStatusCode200ParaListarTodasBebidas() throws Exception {
        // Arrange
        List<Bebida> bebidas = Arrays.asList(
                mock(Bebida.class),
                mock(Bebida.class)
        );
        when(bebidaService.findAll()).thenReturn(bebidas);

        // Act
        var response = mockMvc.perform(
                get("/api/bebidas")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    static Stream<Arguments> provideValidBebidaDTO() {
        var bebida = new BebidaDTO("Cerveja",TipoBebida.ALCOOLICA.name(), 1L);
        var bebida3 = new BebidaDTO("",TipoBebida.ALCOOLICA.name(), 1L);
        return Stream.of(
                Arguments.of(bebida, HttpStatus.CREATED.value()),
                Arguments.of(bebida3, HttpStatus.BAD_REQUEST.value())
        );
    }

    private static Stream<Arguments> provideRespostas() {
        return Stream.of(
                Arguments.of(1L, HttpStatus.OK.value()),
                Arguments.of(null, HttpStatus.NOT_FOUND.value()),
                Arguments.of(0L, HttpStatus.NOT_FOUND.value())
        );
    }

}