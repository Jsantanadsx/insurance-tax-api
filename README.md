# Insurance Tax API

API REST desenvolvida em Java com Spring Boot para cadastro e atualização de produtos de seguro com cálculo automático do preço tarifado a partir das taxas de IOF, PIS e COFINS aplicáveis a cada categoria.

A aplicação mantém as taxas em banco de dados, calcula o valor tarifado no backend e persiste os produtos utilizando H2, Spring Data JPA e Flyway.

---

## Objetivo

O objetivo da aplicação é receber produtos de seguro contendo:

- nome;
- categoria;
- preço base.

A partir desses dados, o sistema identifica as taxas vigentes para a categoria do seguro e calcula automaticamente o preço tarifado.

O cliente não define o `preco_tarifado`. Esse valor é sempre calculado pelo backend com base nas regras e taxas vigentes da aplicação.

---

## Tecnologias utilizadas

- Java 17
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- H2 Database
- Flyway
- Lombok
- Maven
- JUnit 5
- Mockito
- MockMvc
- JaCoCo
- Spring Boot Actuator

---

## Categorias de seguro

A aplicação aceita as seguintes categorias:

```text
VIDA
AUTO
VIAGEM
RESIDENCIAL
PATRIMONIAL
```

Foi utilizado um `enum` para restringir as categorias aos valores definidos pela aplicação e impedir o processamento de categorias inválidas.

---

## Impostos utilizados

O cálculo do preço tarifado considera três impostos:

```text
IOF
PIS
COFINS
```

As taxas não ficam fixas diretamente na lógica de cálculo.

Elas são armazenadas no banco de dados e possuem uma data de início de vigência. Dessa forma, a aplicação pode trabalhar com novas versões das taxas sem necessidade de alterar a regra de cálculo.

---

## Taxas iniciais

As taxas iniciais da aplicação são inseridas automaticamente pelo Flyway.

| Categoria | IOF | PIS | COFINS | Total |
|---|---:|---:|---:|---:|
| VIDA | 1,00% | 2,20% | 0,00% | 3,20% |
| AUTO | 5,50% | 4,00% | 1,00% | 10,50% |
| VIAGEM | 2,00% | 4,00% | 1,00% | 7,00% |
| RESIDENCIAL | 4,00% | 0,00% | 3,00% | 7,00% |
| PATRIMONIAL | 5,00% | 3,00% | 0,00% | 8,00% |

A versão inicial das taxas possui vigência a partir de:

```text
2026-01-01T00:00:00
```

---

## Regra de cálculo

O preço tarifado é calculado utilizando a seguinte fórmula:

```text
preco_tarifado =
    preco_base
    + (preco_base × IOF)
    + (preco_base × PIS)
    + (preco_base × COFINS)
```

Por exemplo, para um seguro da categoria `VIDA` com preço base de `100.00`:

```text
Preço base = 100,00
IOF        = 1,00
PIS        = 2,20
COFINS     = 0,00

Preço tarifado = 103,20
```

Os valores monetários são calculados utilizando `BigDecimal`, evitando problemas de precisão com valores financeiros.

---

## Arquitetura

A aplicação foi organizada separando responsabilidades entre domínio, aplicação, infraestrutura e apresentação.

```text
com.insurance.tax
│
├── application
│   ├── exception
│   ├── port
│   └── service
│
├── domain
│   ├── model
│   └── pricing
│
├── infrastructure
│   ├── config
│   └── persistence
│       ├── adapter
│       ├── entity
│       ├── mapper
│       └── repository
│
└── presentation
    ├── controller
    ├── dto
    │   ├── request
    │   └── response
    └── exception
```

### Fluxo principal

```text
Cliente HTTP
     ↓
Controller
     ↓
Service
     ↓
Regra de domínio
     ↓
Repository Port
     ↓
Repository Adapter
     ↓
Spring Data JPA
     ↓
H2
```

Essa organização mantém as regras de negócio desacopladas da camada de persistência e da interface HTTP.

---

# API de Produtos

Base URL:

```text
http://localhost:8080/api/produtos
```

---

## Criar produto

### Endpoint

```http
POST /api/produtos
```

### Request

```json
{
  "nome": "Seguro de Vida Individual",
  "categoria": "VIDA",
  "preco_base": 100.00
}
```

### Response

Status:

```text
201 Created
```

Exemplo:

```json
{
  "id": "133c5d82-56fb-4b2c-ab52-67de6bafac45",
  "nome": "Seguro de Vida Individual",
  "categoria": "VIDA",
  "preco_base": 100.00,
  "preco_tarifado": 103.20
}
```

O UUID é gerado automaticamente pela aplicação.

O `preco_tarifado` também é calculado automaticamente.

---

## Proteção do preço tarifado

Mesmo que o cliente tente enviar:

```json
{
  "nome": "Seguro de Vida Individual",
  "categoria": "VIDA",
  "preco_base": 100.00,
  "preco_tarifado": 999999.99
}
```

o valor enviado em `preco_tarifado` não é utilizado.

A aplicação continua retornando o valor calculado:

```json
{
  "nome": "Seguro de Vida Individual",
  "categoria": "VIDA",
  "preco_base": 100.00,
  "preco_tarifado": 103.20
}
```

A responsabilidade pelo cálculo pertence exclusivamente ao backend.

---

## Atualizar produto

### Endpoint

```http
PUT /api/produtos/{id}
```

Exemplo:

```http
PUT /api/produtos/eab8db68-9aa6-42d3-9233-d345971b3232
```

### Request

```json
{
  "nome": "Seguro de Vida Premium",
  "categoria": "VIDA",
  "preco_base": 200.00
}
```

### Response

Status:

```text
200 OK
```

Exemplo:

```json
{
  "id": "eab8db68-9aa6-42d3-9233-d345971b3232",
  "nome": "Seguro de Vida Premium",
  "categoria": "VIDA",
  "preco_base": 200.00,
  "preco_tarifado": 206.40
}
```

Durante a atualização:

- o mesmo UUID é mantido;
- os novos dados são utilizados;
- as taxas vigentes são consultadas novamente;
- o preço tarifado é recalculado;
- o registro existente é atualizado.

O processo não cria um segundo produto.

---

## Validação

Os requests de criação e atualização utilizam Bean Validation.

São obrigatórios:

```text
nome
categoria
preco_base
```

Além disso:

```text
nome       → não pode estar vazio
categoria  → não pode ser nula
preco_base → deve ser maior que zero
```

Exemplo de request inválido:

```json
{
  "nome": "",
  "categoria": null,
  "preco_base": -100.00
}
```

Resposta:

```text
400 Bad Request
```

```json
{
  "nome": "nome é obrigatório",
  "categoria": "categoria é obrigatória",
  "preco_base": "preco_base deve ser maior que zero"
}
```

Requests inválidos são bloqueados antes de chegarem ao `ProductService`.

---

## Produto não encontrado

Uma tentativa de atualização utilizando um UUID inexistente retorna:

```text
404 Not Found
```

Exemplo:

```http
PUT /api/produtos/00000000-0000-0000-0000-000000000000
```

Resposta:

```json
{
  "erro": "Product not found: 00000000-0000-0000-0000-000000000000"
}
```

---

# API administrativa de taxas

A aplicação também possui uma API para consultar as taxas vigentes.

### Endpoint

```http
GET /api/admin/taxas
```

Exemplo:

```text
http://localhost:8080/api/admin/taxas
```

A chamada sem parâmetros utiliza a data e hora atual como referência.

---

## Consultar taxas em uma data específica

Também é possível definir uma data de referência:

```http
GET /api/admin/taxas?dataReferencia=2026-10-09T12:00:00
```

Exemplo de resposta:

```json
[
  {
    "id": "uuid",
    "categoriaSeguro": "VIDA",
    "tipoImposto": "IOF",
    "taxa": 0.010000,
    "vigenteDesde": "2026-01-01T00:00:00",
    "criadoPor": "SYSTEM",
    "criadoEm": "2026-10-09T01:37:31.733051"
  }
]
```

O sistema seleciona a versão da taxa aplicável à data informada.

---

# Persistência

A execução local utiliza H2 em modo arquivo.

Configuração:

```text
jdbc:h2:file:./data/insurance-tax-db
```

Isso significa que os dados permanecem disponíveis mesmo após a aplicação ser encerrada e iniciada novamente.

Usuário:

```text
sa
```

Senha: vazia.

---

# H2 Console

Com a aplicação em execução:

```text
http://localhost:8080/h2-console
```

Utilize:

```text
JDBC URL: jdbc:h2:file:./data/insurance-tax-db
User Name: sa
Password:
```

---

# Flyway

O Flyway é responsável pela evolução do banco de dados.

Migrations atuais:

```text
V1__create_insurance_tax_rate.sql
V2__seed_initial_tax_rates.sql
V3__create_insurance_product.sql
```

### V1

Cria a estrutura utilizada para armazenar versões das taxas.

### V2

Insere as taxas iniciais de IOF, PIS e COFINS.

### V3

Cria a estrutura de persistência dos produtos de seguro.

O Hibernate não cria ou modifica automaticamente as tabelas:

```properties
spring.jpa.hibernate.ddl-auto=none
```

A evolução do schema fica sob responsabilidade do Flyway.

---

# Observabilidade

Spring Boot Actuator está habilitado.

Endpoints expostos:

```text
health
info
metrics
```

Exemplo:

```text
http://localhost:8080/actuator/health
```

---

# Como executar

## Pré-requisitos

É necessário possuir:

```text
Java 17
```

O projeto utiliza Maven Wrapper, portanto não é necessário instalar Maven separadamente.

---

## Clonar o projeto

```bash
git clone https://github.com/Jsantanadsx/insurance-tax-api.git
cd insurance-tax-api
```

---

## Executar a aplicação

No Git Bash, Linux ou macOS:

```bash
./mvnw spring-boot:run
```

No Windows PowerShell ou Prompt de Comando:

```powershell
.\mvnw.cmd spring-boot:run
```

Após inicializar:

```text
http://localhost:8080
```

---

# Exemplos com curl

## Criar produto

```bash
curl -i -X POST "http://localhost:8080/api/produtos" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Seguro de Vida Individual",
    "categoria": "VIDA",
    "preco_base": 100.00
  }'
```

---

## Atualizar produto

Substitua `SEU-UUID` pelo identificador retornado na criação:

```bash
curl -i -X PUT "http://localhost:8080/api/produtos/SEU-UUID" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Seguro de Vida Premium",
    "categoria": "VIDA",
    "preco_base": 200.00
  }'
```

---

## Consultar taxas vigentes

```bash
curl "http://localhost:8080/api/admin/taxas"
```

---

# Testes

Para executar toda a suíte:

```bash
./mvnw clean test
```

Estado atual da aplicação:

```text
Tests run: 77
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

A suíte contém testes de:

- domínio;
- cálculo de tarifas;
- serviços;
- repositórios;
- adapters;
- mappers;
- controllers;
- validação HTTP;
- persistência;
- integração com Spring;
- integração com H2 e Flyway.

---

# Cobertura de código

O projeto utiliza JaCoCo.

Após executar:

```bash
./mvnw clean test
```

o relatório HTML pode ser consultado em:

```text
target/site/jacoco/index.html
```

---

# Decisões técnicas

## BigDecimal

Valores monetários e taxas utilizam `BigDecimal` para evitar problemas de precisão associados a `double` e `float`.

## UUID

Os produtos e as versões das taxas utilizam UUID como identificador.

## Taxas persistidas

As taxas não ficam codificadas diretamente na regra de negócio.

Isso permite manter diferentes versões e vigências das taxas sem modificar o código de cálculo.

## Flyway

A estrutura do banco é versionada por migrations, oferecendo uma evolução reproduzível do schema.

## Separação de responsabilidades

Controllers não realizam cálculo ou persistência diretamente.

A camada HTTP apenas recebe requests, valida os dados e delega o processamento para a camada de aplicação.

## Preço tarifado controlado pelo backend

O cliente não possui controle sobre o valor de `preco_tarifado`.

A aplicação sempre realiza um novo cálculo com base em:

```text
categoria
preco_base
taxas vigentes
```

---

# Estado atual

Atualmente a aplicação possui:

```text
[OK] Cadastro de produtos
[OK] Atualização de produtos
[OK] Cálculo automático do preço tarifado
[OK] Persistência H2
[OK] Taxas dinâmicas persistidas
[OK] Versionamento de taxas por vigência
[OK] API administrativa de consulta de taxas
[OK] Flyway
[OK] Bean Validation
[OK] HTTP 400 para dados inválidos
[OK] HTTP 404 para produto inexistente
[OK] Actuator
[OK] Testes unitários
[OK] Testes de integração
[OK] JaCoCo
```

---

# Possíveis Próximas evoluções

Como evolução da solução, podem ser adicionados:

- documentação interativa OpenAPI / Swagger;
- interface web para utilização da API;
- gerenciamento administrativo de novas taxas;
- autenticação e autorização;
- banco de dados externo para ambientes de produção;
- containerização;
- pipeline de CI/CD.

---

## Autor

João Victor Santana dos Santos

Projeto desenvolvido como desafio técnico utilizando Java e Spring Boot.
