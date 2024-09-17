# **API de Controle de Estoque de Bebidas**



# Descrição
A motivação de criação dessa API veio da necessidade e observação em vários depósitos de bebidas em que a gestão do controle de estoque é feito de maneira manual, gerando uma sério de falhas operacionais, ocasionando perdas financeiras e muito retrabalho.

# Funcionalidades

- Cadastro e consulta das bebidas armazenadas em cada seção com suas respectivas queries. 

- Consulta do volume total no estoque por cada tipo de bebida. 

- Consulta dos locais disponíveis de armazenamento de um determinado volume de bebida. (calcular via algoritmo). 

- Consulta das seções disponíveis para venda de determinado tipo de bebida (calcular via algoritmo). 

- Cadastro de histórico de entrada e saída de bebidas em caso de venda e recebimento. 

- Consulta do histórico de entradas e saídas por tipo de bebida e seção. 
As seguintes regras devem ser respeitadas no fluxo de cadastro e cálculo: 

# Regras de Negócio
- Uma seção não pode ter dois ou mais tipos diferentes de bebidas 

- Não há entrada ou saída de estoque sem respectivo registro no histórico. 

- Registro deve conter horário, tipo, volume, seção e responsável pela entrada. 

- Uma seção não pode receber bebidas não alcoólicas se recebeu alcoólicas no mesmo dia. Ex: Seção 2 começou o dia com 50 litros de bebidas alcoólicas que foram consumidas do estoque, só poderá receber não alcoólicas no dia seguinte. 

- O endpoint de consulta de histórico de entrada e saída de estoque, deve retornar os resultados ordenados por data e seção, podendo alterar a ordenação via parâmetros. 
- Para situações de erro, é necessário que a resposta da requisição seja coerente em exibir uma mensagem condizente com o erro.

#
# Tecnologias

- Spring Boot
- Java 17
- MySQL
- Docker
- EntityManager
- Swagger
- Lombok
- jUnit

***

# Arquitetura da Aplicação
Como já dizia o bom e conhecido Robert C. Martin, a sua arquitetura deve informar os leitores sobre o sistema e não sobre os frameworks que você usou no sistema.
Nesse sentido eu tentei minimamente utilizar o padrão Clean Architecture usando Java, Spring, mais a parte de API e acesso a dados.

## Camadas
- core
- data
- domain

## Tratamento de Exceções
Implementei um ControllerAdvice que controla todo o fluxo de exceção, juntamente com mensagens personalizadas de acordo com o tipo do erro.

## Exceções personalizadas
As seguintes exeções personalizdas foram criadas de modo que fique claro quando o erro ocorrer: `AdicionarBebidaException` `BebidaArmazenadaException` `BebidaNotFoundException` `EstoqueBebidasException` `MovimentoInvalidoException` `RemoverVolumeMaiorException` `SaidaEstoqueException` `SecaoInvalidaException` `SecaoNotFoundException` `VolumeInsuficienteException` `VolumeSecaoInvalidaException`

## Padrões Utilizados
Foram utilizados alguns padrões de projeto para facilitar a implementação das regras de negócio e para uma possível mamutenção futura. Então, tudo que foi aplicado, tornou o código mais manutenível e desacoplado. Os padrões utilizados foram:

- `DTO`: Data Transfer Object é um padrão de arquitetura de objetos qe agregam e encapsulam dados para transferência. Ele foi utilizado na API em alguns pontos para representar algumas informações.
- Builder: Nos permite construir objetos complexos de forma organizada e legível, evitando a confusão causada por muitos construtores e métodos setters.

- `Factory method`: Atua como uma fábrica que escolhe que escolhe qual estratégia de movimentação de bebidas deve ser usada com base no tipo de movimento. O Factory Pattern também está presente n implementação da classe **MovimentoHistoricoStrategyFactor**y. O padrão Factory é utilizado para encapsular a lógica de criação de objetos, delegando a responsabilidade de instanciar as classes corretas com base em algum critério (neste caso, o `TipoMovimento`). O Factory Pattern foi utilizado para encapsular a lógica de escolha da estratégia de movimentação aproprieada e a retorna com base no parâmetro TipoMovimento(como ENTRADA ou SAIDA). Dessa maneira, se houver novos tipos no futuro, será possível facilmente adicionar novas estratégias sem alterar o código existente, apenas registrando a nova implementação no `Map`.

- `Chain of Responsibility`: Esse padrão nos permite que criemos uma cadeia de responsabilidades onde cada manipulador pode processar uma solicitação ou passar para o próximo na cadeia. Isso está implementado  nas classes `AbstractHandler ` e `ValidacaoEstoquehandler`.
- `Strategy`: Fornece uma maneira de escolher dinamicamente entre várias implementações de uma interface de acordo com a situação. Nessa API em específico o padrão **Strategy** está sendo utilizado para a lógica de movimentação de bebidas e validações, como em `MovimentoHistoricoStrategyFactory`.

#**Em Resumo:**
**Interface** `HandlerEstoque`, `AbstractHandler`, e `ValidacaoEstoquehandler` fazem parte do padrão **Chain of Responsibility**. Eles permitem que encadeemos múltiplos manipuladores de lógica de negócio.

**Movimento de Bebidas e Validações (Strategy)**: O Strategy Pattern está presente na forma como escolhemos a estratégia de movimentação de bebidas (**MovimentoHistoricoStrategyFactory**) e na forma como lida com validações específicas de entrada ou saída de bebidas (**ValidacaoEstoquehandler, ValidacaoSaidaHandler**).

#Resumo de Padrões na Implementação:

**Chain of Responsibility**: Usado para criar uma cadeia de validações e processamento, como na AbstractHandler e seus subclasses.
**Strategy Pattern**: Utilizado para selecionar a lógica de movimentação de bebidas e as validações de entrada ou saída.
**Factory Pattern**: A classe MovimentoHistoricoStrategyFactory é uma implementação clássica do Factory Pattern, escolhendo e retornando a estratégia correta de movimentação de acordo com o tipo de operação (TipoMovimento).
Esses três padrões trabalham juntos para criar uma arquitetura flexível e escalável para sua aplicação.

# Execução via Docker
A aplicação pode ser executada da maneira tradicional, baixando do git, importando o projeto na IDE de preferência e rodando a aplicação. Não esquecendo de ter de configurar um banco de dados.
Mas, existe uma maneira mais interesante e adequada que é a utilização de contêniers via Docker para empacotar, buildar e subir a aplicação.
Primeiramente precisamos ter um arquivo Dockefile na raiz do projeto, conforme pode ser observado nos arquivos do projeto com as configurações base para ele ser executado.
Tendo isso em mãos, no cmd da raiz do projeto, entre os seguintes comandos: 
- `docker build -t estoque-bebidas-app .`-> com esse comando a gente específica o nome da imagem e o ponto "." indica para o Docker build onde vai estar o arquivo Dockerfile.
- `docker images` -> verifica a imagem criada
- `docker run -p 8080:8080 estoque-bebidas-app`-> O conteiner vai rodar numa aplicação spring boot na porta 8080:8080.
- `docker-compose up` -> rode esse comando caso queira que suba no conteiner a aplicação junto com o banco de dados.

## Perguntass Respostas
## 1 - O que achou do teste? Grau de dificuldade, desafios encontrados, etc.
Achei um grau médio,porém as regras de negócio estavam um tanto confusas em alguns pontos. Meu maior desafio foi conciliar minhas tarefas diárias de trabalho com esse desafio. Isso o tornoo, sem trocadilhos, bastante desafiador, principalmente porque tentei entregar uma API mais robusta que o normal e com um código com mais qualidade.

## 2 -  Alteraria algo no teste para analisar alguma outra habilidade?
Se houvesse tempo ágil, talvez trabalhar com estrutura de filas.

## 3 - Entende que testes unitários são necessários para garantia da qualidade do código entregue?
Entendo e sou totalmente favorável de sua utilização, pois além de nos ensinar a pegar o hábito de escrevê-los, nos permite mapear pequenas partes de um código uilizando os testes unitários, assim como os de integração cobrindo de ponta a ponta uma transação, desde a chamada da controller até a persistência na base de dados.
