package com.magis5.estoquedebebidas.domain.service;

import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.data.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.usecase.chains.implementations.MovimentacaoBebidas;
import com.magis5.estoquedebebidas.domain.validators.implementations.SecaoValidadorChain;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("tests")
class SecaoServiceTest {

    @Autowired
    private EntityManager manager;

    @Mock
    private EntityManager entityManager;

    @Mock
    private BebidaService bebidaService;

    @Mock
    private MovimentacaoBebidas movimentacaoBebidas;

    @Mock
    private HistoricoService historicoService;

    @Mock
    private TiposConsultaSecaoService tiposConsultaSecaoService;

    @InjectMocks
    private SecaoService secaoService;

    @Autowired
    private SecaoService service;

    @Mock
    private SecaoValidadorChain validatorChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Order(1)
    @Test
    @DisplayName("Criando Seção integrada com base de dados")
    void deveCriarSecaoComSucesso() {
        // Dado um SecaoDTO válido
        SecaoDTO secaoDTO = new SecaoDTO(
                1, TipoBebida.ALCOOLICA, 500, 50
        );

        // Quando o método criarSecao é chamado
        Secao secao = service.criarSecao(secaoDTO);

        // Então o Secao deve ser persistido no banco
        assertNotNull(secao.getId()); // Verifica se o ID foi gerado
        assertEquals(1, secao.getNumSecao());
        assertEquals(TipoBebida.ALCOOLICA, secao.getTipoBebida());
        assertEquals(secaoDTO.capacidadeMaxima(), secao.getCapacidadeMaxima());
        assertEquals(secaoDTO.volume(), secao.getVolumeAtual());

        // Verifica se a secao foi persistida de fato no banco de dados
        Secao secaoPersistida = manager.find(Secao.class, secao.getId());
        assertNotNull(secaoPersistida);
        assertEquals(TipoBebida.ALCOOLICA, secaoPersistida.getTipoBebida());
    }
    @Order(2)
    @Test
    @DisplayName("Consulta integrada com base de dados de uma Seção pelo ID da Seção")
    void deveRetornarAsInformacoesDaSecaoPeloId() {
        // Recupera a seção pelo ID
        Secao buscaSecao = service.getBySecaoId(1L);

        // Verifica se a seção recuperada não é nula
        assertNotNull(buscaSecao, "A seção não deve ser nula");

        // Verifica se o ID é igual ao ID da seção criada
        assertEquals(1L, buscaSecao.getId(), "Os IDs devem ser iguais");

        // Verifica se os demais campos estão corretos
        assertEquals(1, buscaSecao.getNumSecao(), "Os números da seção devem ser iguais");
        assertEquals(TipoBebida.ALCOOLICA, buscaSecao.getTipoBebida(), "Os tipos de bebida devem ser iguais");
        assertEquals(500.0, buscaSecao.getCapacidadeMaxima(), "As capacidades máximas devem ser iguais");
        assertEquals(50.0, buscaSecao.getVolumeAtual(), "Os volumes atuais devem ser iguais");
    }

    @Test
    @DisplayName("Consulta integrada de uma Seção pelo ID da seção sem Order")
    void deveRetornarAsInformacoesDaSecaoPeloIdSemOrdemDeExecucao() {
        // Cria uma nova seção e persiste no banco de dados
        SecaoDTO secaoDTO = new SecaoDTO(1, TipoBebida.ALCOOLICA, 500, 50);
        Secao secao = service.criarSecao(secaoDTO);

        // Recupera a seção pelo ID
        Secao buscaSecao = service.getBySecaoId(1L);

        // Verifica se a seção recuperada não é nula
        assertNotNull(buscaSecao, "A seção não deve ser nula");

        // Verifica se o ID é igual ao ID da seção criada
        assertEquals(1L, buscaSecao.getId(), "Os IDs devem ser iguais");

        //Verifica se os demais campos estão corretos
        assertEquals(secaoDTO.numero(), buscaSecao.getNumSecao(), "Os números da seção devem ser iguais");
        assertEquals(secaoDTO.tipoBebida(), buscaSecao.getTipoBebida(), "Os tipos de bebida devem ser iguais");
        assertEquals(secaoDTO.capacidadeMaxima(), buscaSecao.getCapacidadeMaxima(), "As capacidades máximas devem ser iguais");
        assertEquals(secaoDTO.volume(), buscaSecao.getVolumeAtual(), "Os volumes atuais devem ser iguais");
    }

    @Test
    @DisplayName("Criando Seção mockada")
    void criarSecao() {
        // Arrange
        SecaoDTO secaoDTO = new SecaoDTO(1, TipoBebida.ALCOOLICA, 100, 50);
        Secao expectedSecao = Secao.builder()
                .numSecao(secaoDTO.numero())
                .tipoBebida(secaoDTO.tipoBebida())
                .capacidadeMaxima(secaoDTO.capacidadeMaxima())
                .volumeAtual(secaoDTO.volume())
                .build();

        // Simula o comportamento de validação bem-sucedida
        doNothing().when(validatorChain).validate(secaoDTO);

        // Simula o comportamento do método persist
        doNothing().when(entityManager).persist(expectedSecao);

        // Act
        Secao resultSecao = secaoService.criarSecao(secaoDTO);

        // Assert
        assertNotNull(resultSecao);
        assertEquals(expectedSecao.getNumSecao(), resultSecao.getNumSecao());
        assertEquals(expectedSecao.getTipoBebida(), resultSecao.getTipoBebida());
        assertEquals(expectedSecao.getCapacidadeMaxima(), resultSecao.getCapacidadeMaxima());
        assertEquals(expectedSecao.getVolumeAtual(), resultSecao.getVolumeAtual());

    }
    @Test
    @DisplayName("Verificando quando um número for zero")
    void deveLancarExcecaoQuandoNumeroSecaoForZero() {
        // Arrange
        SecaoDTO secaoDTO = new SecaoDTO(0, TipoBebida.ALCOOLICA, 100.0, 50.0);
        // Act & Assert
        assertThrows(SecaoInvalidaException.class, () -> secaoService.criarSecao(secaoDTO));
    }

    @Test
    @DisplayName("Validando retorno de exceção")
    void testCriarSecao_ValidationError() {
        // Arrange
        SecaoDTO secaoDTO = new SecaoDTO(1, TipoBebida.ALCOOLICA, 600, 50);

        // Simula uma exceção de validação
        doThrow(new SecaoInvalidaException("Houve um erro em uma ou mais regras de validação"))
                .when(validatorChain).validate(secaoDTO);

        // Act & Assert
        SecaoInvalidaException thrown = assertThrows(SecaoInvalidaException.class, () -> {
            secaoService.criarSecao(secaoDTO);
        });

        assertEquals("secao inválida: Houve um erro em uma ou mais regras de validação", thrown.getMessage());
    }
}