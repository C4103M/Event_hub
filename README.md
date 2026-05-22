# EventHub

Bem-vindo ao repositório oficial do EventHub, um sistema completo para gerenciamento de eventos, venda de ingressos e controle de acesso.

O projeto foi desenvolvido para as disciplinas de LP1 (Linguagem de Programação 1) e APSI (Análise e Projeto de Sistemas), aplicando boas práticas de engenharia de software, com foco em organização, escalabilidade e separação de responsabilidades.

A arquitetura segue o padrão MVC (Model-View-Controller), complementado por uma camada de serviços e persistência com JPA/Hibernate.

---

## Estrutura do Repositório

```bash
event-hub/
├── 1.Requisitos/
│   ├── historias_de_usuario.txt
│   └── levantamento_requisitos.pdf
│
├── 2.Analise/
│   ├── diagrama_classes.png
│   ├── diagrama_casos_uso.png
│   └── diagrama_arquitetura.png
│
└── 3.Implementacao/
    └── hexanet/
        ├── pom.xml
        └── src/
            ├── main/
            │   ├── java/org/hexanet/eventhub/
            │   │   ├── controller/
            │   │   ├── dao/
            │   │   ├── dto/
            │   │   ├── model/
            │   │   ├── service/
            │   │   ├── singleton/
            │   │   └── util/
            │   │
            │   └── resources/
            │       ├── META-INF/persistence.xml
            │       └── auth/
            │           ├── Cadastro.fxml
            │           └── Login.fxml
            │
            └── test/java/
```

---

## Arquitetura da Implementação

O código está organizado em pacotes bem definidos, cada um com responsabilidade clara:

### controller/
Responsável por intermediar a comunicação entre a interface (JavaFX) e a aplicação.

- Captura eventos da UI (@FXML)
- Coleta dados dos formulários
- Encaminha ações para a camada de serviço

---

### dao/
Camada de acesso a dados (Data Access Object).

- Executa operações diretas no banco
- Implementa CRUD genérico via BaseCrud<T>
- Isolada da lógica de negócio

---

### dto/
Objetos de transferência de dados (Data Transfer Objects).

- Estruturas simples (sem lógica)
- Transportam dados da UI → Service
- Exemplo: CadastroUsuarioDTO

---

### service/
Camada de regras de negócio (núcleo da aplicação).

- Validações
- Criptografia de senha (BCrypt)
- Controle de transações (EntityTransaction)
- Conversão DTO → Entidade

---

### model/
Entidades de domínio mapeadas com JPA.

- Representam o banco de dados
- Herança com estratégia JOINED:
   - Usuario (classe base)
   - Participante e Organizador (especializações)

---

### singleton/
Gerenciamento de estado global da aplicação.

- SessaoUsuario mantém o usuário autenticado
- Implementa o padrão Singleton

---

### util/
Utilitários e ferramentas auxiliares.

- Classes sem estado
- Exemplo:
   - ScreenManager: gerencia navegação entre telas JavaFX

---

## Decisões de Design

### Controle de Concorrência (@Version)
Uso de Optimistic Locking na entidade Evento para evitar:

- Venda duplicada de ingressos
- Problemas em acessos simultâneos

---

### Persistência em Cascata
Relacionamento entre entidades (ex: Pedido → Pagamento):

- CascadeType.ALL
- Garante consistência transacional

---

### Segurança de Senhas
Uso de BCrypt para:

- Hash seguro de senhas
- Proteção contra vazamento de credenciais

---

### Isolamento de Camadas

- DAO não conhece DTO
- Service não depende de JavaFX
- UI desacoplada da lógica de negócio

Isso melhora testabilidade, manutenção e escalabilidade.

---

## Como Executar o Projeto

### Pré-requisitos

- Java JDK 17+
- Apache Maven
- Banco de dados configurado conforme persistence.xml

---

### Passos

1. Atualize as dependências do Maven:
```bash
mvn clean install
```

2. Compile o projeto:
```bash
mvn clean compile
```

3. Execute pela sua IDE (classe principal JavaFX)

---

## Possíveis Melhorias Futuras

- Implementar testes automatizados (JUnit + Mockito)
- Adicionar autenticação com JWT (caso vire API)
- Criar versão web (Spring Boot)
- Logging estruturado (SLF4J + Logback)
- Tratamento global de exceções

---

## Observações finais

O projeto já utiliza boas práticas importantes como separação em camadas, uso de DTO, controle de concorrência e criptografia, o que contribui para qualidade, segurança e organização do código.
