package com.magis5.estoquedebebidas.application.service;

import com.magis5.estoquedebebidas.core.exceptions.MovimentoInvalidoException;
import com.magis5.estoquedebebidas.adapters.models.MovimentoBebidasRequest;
import com.magis5.estoquedebebidas.domain.entities.Bebida;
import com.magis5.estoquedebebidas.domain.entities.Historico;
import com.magis5.estoquedebebidas.domain.entities.Secao;
import com.magis5.estoquedebebidas.domain.enums.TipoMovimento;
import com.magis5.estoquedebebidas.domain.repositories.HistoricoRepositoryCustom;
import com.magis5.estoquedebebidas.application.usecases.factories.MovimentoHistoricoStrategyFactory;
import com.magis5.estoquedebebidas.application.usecases.strategies.interfaces.MovimentoHistoricoStrategy;
import com.magis5.estoquedebebidas.application.usecases.validators.implementations.ValidacaoEntradaHandler;
import com.magis5.estoquedebebidas.application.services.HistoricoService;
import com.magis5.estoquedebebidas.application.services.SecaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("tests")
class HistoricoServiceTest {

    @Spy
    @InjectMocks
    private HistoricoService historicoService;

    @Mock
    private HistoricoRepositoryCustom historicoRepositoryCustom;

    @Mock
    private MovimentoHistoricoStrategyFactory strategyFactory;

    @Mock
    private MovimentoHistoricoStrategy strategy;

    @Mock
    private SecaoService secaoService;

    @Mock
    private ValidacaoEntradaHandler validacaoEntradaHandler;

    @Test
    @DisplayName("Consulta o histórico ordenado por seção e data")
    void consultaHistoricoOrderBySecaoDataAsc() {
        // Dados de entrada
        String sortField = "secao";
        String sortDirection = "asc";
        Integer numSecao = 1;
        String tipoMovimento = "ENTRADA";

        // Mock da lista de históricos, incluindo um com TipoMovimento nulo
        List<Historico> mockHistoricoList = Arrays.asList(
                criarHistorico(1, TipoMovimento.ENTRADA),
                criarHistorico(1, null),
                criarHistorico(2, TipoMovimento.SAIDA)
        );
        when(historicoRepositoryCustom.findHistorico(sortField, sortDirection)).thenReturn(mockHistoricoList);

        // Executa o método

        List<Historico> resultado = historicoService.consultaHistoricoOrderBySecaoDataAsc(sortField, sortDirection, numSecao, tipoMovimento);

        // Valida o resultado
        assertEquals(1, resultado.size());
        assertEquals("ENTRADA", resultado.get(0).getTipoMovimento().name());
        assertEquals(1, resultado.get(0).getSecao().getNumSecao());
    }

    @Test
    void testAtualizarHistoricoValidacaoEntrada() {
        Secao secao = new Secao();
        Bebida bebida = new Bebida();
        MovimentoBebidasRequest request = new MovimentoBebidasRequest("Allan", TipoMovimento.ENTRADA, 100.0);

        when(strategyFactory.getStrategy(TipoMovimento.ENTRADA)).thenReturn(strategy);

        doReturn(validacaoEntradaHandler).when(historicoService).getAbstractHandler(TipoMovimento.ENTRADA);
        doNothing().when(validacaoEntradaHandler).handle(any(Bebida.class), any(MovimentoBebidasRequest.class));

        // Configurando o comportamento do método handle
        doNothing().when(validacaoEntradaHandler).handle(any(Bebida.class), any(MovimentoBebidasRequest.class));

        historicoService.atualizarHistorico(secao, bebida, request);

        // Verificação
        verify(validacaoEntradaHandler).handle(bebida, request);
        verify(strategy).registrar(secao, bebida, request);
    }

    @Test
    @DisplayName("Teste que atualiza o histórico após validação")
    void testAtualizarAposValidacao() {
        Secao secao = new Secao();
        Bebida bebida = new Bebida();
        MovimentoBebidasRequest request = new MovimentoBebidasRequest("Allan", TipoMovimento.ENTRADA, 100.0);

        when(strategyFactory.getStrategy(TipoMovimento.ENTRADA)).thenReturn(strategy);

        // Act
        historicoService.atualizar(secao, bebida, request);

        verify(strategy).registrar(secao, bebida, request);

    }
    @Test
    @DisplayName("O teste deve lançar uma exceção se o strategy for igual a NULL")
    void testDeveLancarMovimentoInvalidoExceptionQuandoStrategyForNull() {
        // Arrange
        Secao secao = new Secao();
        Bebida bebida = new Bebida();
        MovimentoBebidasRequest request = new MovimentoBebidasRequest("Allan", TipoMovimento.ENTRADA, 100.0);

        // Simular que a factory retorna null
        when(strategyFactory.getStrategy(TipoMovimento.ENTRADA)).thenReturn(null);

        // Act & Assert
        MovimentoInvalidoException exception = assertThrows(MovimentoInvalidoException.class, () -> {
            historicoService.atualizar(secao, bebida, request);
        });

        assertEquals(String.format("Tipo de movimento inválido: %s", TipoMovimento.ENTRADA.name()), exception.getMessage());
    }

    @Test
    @DisplayName("Verifica se pode cadastrar uma bebida na seção")
    void testParaVerificarSePodeSerCadastradaUmaBebidaNaSecao() {
        // Arrange
        String tipoBebida = "ALCOOLICA";
        Long secaoId = 1L;
        boolean expectedResult = true;

        // Configura o mock para retornar o valor esperado
        when(historicoRepositoryCustom.verificarSePodeCadastrarBebidaSecao(tipoBebida, secaoId))
                .thenReturn(expectedResult);

        // Act
        boolean result = historicoService.verificarSePodeCadastrarBebidaSecao(tipoBebida, secaoId);

        // Assert
        assertTrue(result);
        verify(historicoRepositoryCustom, times(1)).verificarSePodeCadastrarBebidaSecao(tipoBebida, secaoId);
    }
    private Historico criarHistorico(Integer secaoNum, TipoMovimento tipoMovimento) {
        Secao secao = new Secao();
        secao.setNumSecao(secaoNum);
        Historico historico = null;
        if (tipoMovimento != null) {
            historico = Historico.builder()
                    .secao(secao)
                    .tipoMovimento(tipoMovimento)
                    .build();
        } else {
            historico = Historico.builder()
                    .tipoMovimento(null)
                    .build();
        }
        return historico;
    }
}