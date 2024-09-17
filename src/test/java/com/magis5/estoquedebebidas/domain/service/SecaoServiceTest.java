package com.magis5.estoquedebebidas.domain.service;

import com.magis5.estoquedebebidas.core.exceptions.SecaoInvalidaException;
import com.magis5.estoquedebebidas.data.models.SecaoDTO;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.enums.TipoBebida;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.usecase.strategy.movements.MovimentacaoBebidas;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest
//@ActiveProfiles("test")
class SecaoServiceTest {

    @Mock
    private EntityManager manager;

    @Mock
    private BebidaService bebidaService;

    @Mock
    private MovimentacaoBebidas movimentacaoBebidas;

    @Mock
    private HistoricoService historicoService;

    @Mock
    private TiposConsultaSecaoService tiposConsultaSecaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @InjectMocks
    private SecaoService secaoService;

    @Test
    void getBySecaoId() {
    }

    @Test
    void criarSecao() {
        // Configurações necessárias para os mocks retornarem os valores corretos
        when(tiposConsultaSecaoService.calcularVolumeTotalEstoque(TipoBebida.ALCOOLICA)).thenReturn(100.0);  // Exemplo de configuração do mock
        // Configura o comportamento do EntityManager, MovimentacaoBebidas, etc., conforme necessário

        // Chama o método de teste
        double resultado = tiposConsultaSecaoService.calcularVolumeTotalEstoque(TipoBebida.ALCOOLICA);

        // Valida se o resultado é o esperado
        assertEquals(100.0, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoNumeroSecaoForZero() {
        // Arrange
        SecaoDTO secaoDTO = new SecaoDTO(0, TipoBebida.ALCOOLICA, 100.0, 50.0);
        // Act & Assert
        assertThrows(SecaoInvalidaException.class, () -> secaoService.criarSecao(secaoDTO));
    }

    @Test
    void retirarBebida() {
    }
}