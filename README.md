# Sistema de Avaliação de Crédito / Aprovação de Empréstimo

Desafio de Padrões de Projeto (Design Patterns) - implementação de uma arquitetura
de microsserviços com Spring Boot, aplicando Strategy, Chain of Responsibility,
Facade e Repository Pattern num cenário de negócio realista.

## Primeiros passos

Ao decidir fazer esse desafio, meu processo foi:

**Entendendo o problema:** o objetivo era demonstrar a aplicação prática de
Padrões de Projeto, não construir um sistema de crédito real. A partir disso,
defini que o projeto precisava ter um domínio de negócio plausível, mas simples
o suficiente para que os padrões aparecessem de forma natural, sem ficar forçado.

**Dividindo em partes menores:** separei o problema assim:
- Cálculo de score de crédito (regra que muda por tipo de cliente)
- Validação dos dados de entrada de um pedido de empréstimo
- Comunicação entre dois serviços independentes
- Decisão de aprovação/reprovação do empréstimo
- Persistência do histórico de avaliações

**Priorizando:** como o foco do desafio é Design Patterns, priorizei assim:
1. `servico-score` completo e testado - Concluído
2. Chain of Responsibility (validação) - Concluído
3. Strategy de aprovação - Concluído
4. Facade orquestrando tudo - Concluído
5. Persistência (H2) - Concluído
6. Endpoint de histórico - Concluído

## Sobre o histórico de commits

Optei por commits pequenos e incrementais, seguindo o padrão **Conventional
Commits** (`feat:`, `fix:`, `chore:`, `docs:`, `wip:`), para padronizar e deixar
o histórico legível.

A quantidade de commits foi intencional. Quis deixar claro o progresso e o
raciocínio por trás de cada decisão - inclusive os momentos em que uma peça
ficou incompleta (`wip:`) e só foi fechada depois. Cada commit representa uma
etapa ou decisão concluída, na ordem real em que o projeto foi construído: de
fora pra dentro (DTOs, depois Controller, depois as camadas internas), serviço
por serviço.

## Arquitetura

O sistema é composto por dois microsserviços independentes que se comunicam de
forma **síncrona** via OpenFeign:

- **servico-score** (porta `8080`): calcula o score de crédito de um cliente PF ou PJ.
- **servico-emprestimo** (porta `8081`): recebe o pedido de empréstimo, valida os
  dados, consulta o score no `servico-score`, decide se o empréstimo é aprovado
  e persiste o resultado.

```
Cliente -> servico-emprestimo (Gateway) -> [Feign] -> servico-score
```

## Padrões de Projeto aplicados

| Padrão | Onde | Por quê |
|---|---|---|
| **Strategy** | `servico-score/strategy`, `servico-emprestimo/strategy` | A regra de cálculo de score e a regra de aprovação mudam conforme o tipo de cliente (PF/PJ). Em vez de um método cheio de `if/else`, cada regra é uma classe isolada, seguindo o princípio Open/Closed - um novo tipo de cliente não exige alterar código já testado |
| **Chain of Responsibility** | `servico-emprestimo/validacao` | As validações do pedido (documento, dados obrigatórios por tipo, valor solicitado) são elos independentes de uma cadeia. Cada elo cuida da sua própria regra e repassa para o próximo |
| **Facade** | `servico-emprestimo/facade/EmprestimoFacade` | Orquestra validação, chamada ao score, decisão de aprovação e persistência numa única chamada simples para o Controller. O Controller não precisa conhecer nenhuma dessas etapas internas |
| **Repository** | `servico-emprestimo/repository/EmprestimoRepository` | Abstrai o acesso a dados por trás de uma interface simples, sem expor detalhes de SQL/JPA para o resto da aplicação |

## Tecnologias

- Java 21
- Spring Boot 3.3.4
- Spring Cloud OpenFeign
- Spring Data JPA + H2 Database
- Spring Validation
- Springdoc OpenAPI (Swagger)
- Lombok

## Decisões técnicas

**Regras de score e aprovação fictícias:** não existe uma fórmula pública de
score de crédito - bureaus como Serasa e SPC usam modelos proprietários. Como o
objetivo aqui é demonstrar Design Patterns, criei fórmulas simples e didáticas
apenas para dar contexto de negócio ao Strategy Pattern. Isso está documentado
como comentário direto nas classes de regra.

**Dois DTOs de Score no `servico-emprestimo` (`ScoreRequestDTO`/`ScoreResponseDTO`):**
esses DTOs "espelham" o contrato do `servico-score`, mas são classes próprias,
não compartilhadas entre os dois serviços. Isso é proposital: em microsserviços,
cada serviço deve ser independente, sem depender de um módulo compartilhado que
acoplaria o deploy de um ao do outro. A pequena duplicação de contrato é um
trade-off aceito em troca de desacoplamento.

**Banco de dados só no `servico-emprestimo`:** o `servico-score` é uma
calculadora stateless - recebe, calcula, devolve, sem nada para persistir. Só
faz sentido ter banco onde existe necessidade real de negócio: guardar o
histórico de avaliações de empréstimo.

**H2 em memória:** como o projeto não tem a intenção de manter dados entre
execuções (é um projeto de estudo/portfólio, não produção), optei pelo H2 em
memória (`jdbc:h2:mem`) em vez de arquivo ou um banco externo como PostgreSQL -
sobe sem nenhuma dependência de infraestrutura externa, o que facilita a
execução por quem for avaliar o projeto.

## O que aprendi

Já tinha contato prévio com o conceito de microsserviços (inclusive um projeto
anterior com comunicação assíncrona via RabbitMQ), mas o **OpenFeign** e o
**Swagger/OpenAPI** eram novidades - nunca tinha usado nenhum dos dois antes
desse desafio. Foi a primeira vez que:
 
- Configurei um cliente HTTP declarativo (Feign) para comunicação síncrona
  entre serviços, sem escrever nenhuma linha de `RestTemplate`/`WebClient` na mão
- Testei e documentei uma API via Swagger direto no navegador, sem precisar de
  Postman

Além disso:
 
- Como o Spring gerencia Beans e injeção de dependência automaticamente a
  partir de `@Component`
- Como o OpenFeign substitui a necessidade de escrever um cliente HTTP manual
  (RestTemplate/WebClient) para comunicação entre microsserviços
- Como funciona o Repository Pattern na prática através do Spring Data JPA
- Diferença entre `application.properties` e `application.yml`
- Fluxo de trabalho profissional com Git: commits pequenos, mensagens
  padronizadas (Conventional Commits), uso de `--amend` e `rebase -i` para
  manter o histórico limpo

## Desafios enfrentados

### NullPointerException por Strategy incompleta

Depois de montar a `EmprestimoFacade`, o endpoint `POST /emprestimos` retornava
erro 500 com `NullPointerException` na linha que buscava a regra de aprovação
no `Map<TipoCliente, RegraAprovacao>`. Investigando o stack trace, percebi que
as implementações concretas do Strategy (`RegraAprovacaoPF`/`RegraAprovacaoPJ`)
nunca tinham sido criadas de fato - só a interface existia. Sem a anotação
`@Component` nessas classes, o Spring nunca as registrou como Beans, e a lista
injetada no construtor da Facade chegava vazia. Criei as duas implementações
concretas e o erro foi resolvido. Esse foi um bom lembrete de que ler/escrever
código não garante que ele existe de fato no projeto - só testar de ponta a
ponta comprova isso.

### H2 Console incompatível com Spring Boot 3

Ao tentar acessar `/h2-console` para visualizar os dados persistidos, recebia
um erro 404 (`No static resource h2-console`), mesmo com a dependência e a
configuração corretas no `application.properties`. Investigando, descobri que
a classe `WebServlet` do H2 (usada internamente pelo console) ainda estende
`javax.servlet.http.HttpServlet`, o namespace antigo, enquanto o Spring Boot 3+
usa o namespace novo `jakarta.servlet`. Essa incompatibilidade impede o
registro automático (e também manual) do console nessa combinação de versões.
Em vez de insistir numa solução frágil, optei por um caminho melhor: criei um
endpoint próprio `GET /emprestimos` que lista o histórico de avaliações
diretamente pela API - resolve o problema original (visualizar os dados
salvos) e ainda agrega uma funcionalidade real e testável no Swagger, sem
depender de uma ferramenta com bug de compatibilidade.

### Organizando o histórico de commits

Durante o debug do problema acima, acabei recriando as classes de Strategy que
já pensava ter criado antes, gerando dois commits quase idênticos no histórico.
Usei `git rebase -i` para remover o commit duplicado antes de enviar ao GitHub,
mantendo o histórico fiel ao que realmente ficou no código final.

## Estrutura do projeto

```
desafio-nttdata/
├── servico-score/
│   ├── controller/       -> POST /score
│   ├── service/          -> orquestra a escolha da Strategy
│   ├── strategy/         -> RegraScorePF, RegraScorePJ
│   └── dto/
└── servico-emprestimo/
    ├── controller/        -> POST /emprestimos, GET /emprestimos
    ├── facade/            -> EmprestimoFacade (orquestra tudo)
    ├── validacao/         -> Chain of Responsibility
    ├── strategy/          -> RegraAprovacaoPF, RegraAprovacaoPJ
    ├── cliente/           -> ScoreClient (OpenFeign)
    ├── repository/        -> EmprestimoRepository
    ├── entity/            -> EmprestimoEntity
    ├── config/            -> CadeiaValidacaoConfig
    ├── exception/         -> ValidacaoException + handler global
    └── dto/
```

## Como rodar

### Pré-requisitos
Java 21 e Maven instalados.

### Primeiro passo - servico-score
Dentro da pasta `servico-score`:
```
mvn spring-boot:run
```
Porta: `8080`

### Segundo passo - servico-emprestimo
Dentro da pasta `servico-emprestimo` (depende do servico-score já estar de pé):
```
mvn spring-boot:run
```
Porta: `8081`

**Ordem para rodar:** servico-score -> servico-emprestimo

### Documentação Swagger
- Score: http://localhost:8080/swagger-ui.html
- Empréstimo: http://localhost:8081/swagger-ui.html

## Exemplos de requisição

`POST http://localhost:8081/emprestimos` (Pessoa Física)
```json
{
  "tipoCliente": "PF",
  "documento": "123.456.789-00",
  "valorSolicitado": 15000,
  "idade": 30,
  "rendaMensal": 5000
}
```

`POST http://localhost:8081/emprestimos` (Pessoa Jurídica)
```json
{
  "tipoCliente": "PJ",
  "documento": "12.345.678/0001-90",
  "valorSolicitado": 200000,
  "faturamentoAnual": 800000,
  "tempoMercadoAnos": 5
}
```

`GET http://localhost:8081/emprestimos` - lista o histórico de todas as
avaliações já processadas.

## Considerações finais

Esse desafio me fez praticar Design Patterns não como teoria isolada, mas
resolvendo um problema de negócio de ponta a ponta, incluindo debugar um erro
real em produção local (o NullPointerException por Strategy incompleta) e
lidar com uma incompatibilidade de versão que não tinha solução "de manual"
(o H2 Console). O código atual cobre os quatro padrões propostos e está
testado manualmente via Swagger, mas sei que ainda há espaço para evoluir:
testes automatizados (JUnit/AssertJ), Docker Compose para subir os dois
serviços juntos, e talvez um terceiro tipo de cliente para provar na prática a
extensibilidade que o Strategy Pattern promete.
