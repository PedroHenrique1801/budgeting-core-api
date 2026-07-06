Markdown<div align="center">

# Budgeting AI API

**Uma API REST para gestão financeira inteligente orientada a Inteligência Artificial.**

**Construída do jeito certo — Arquitetura em camadas, processamento assíncrono de áudio, integração com LLMs, sem atalhos.**



<img src="https://img.shields.io/badge/java-17_LTS-orange?logo=openjdk&logoColor=white" alt="Java" />
<img src="https://img.shields.io/badge/spring_boot-3.x-brightgreen?logo=springboot&logoColor=white" alt="Spring Boot" />
<img src="https://img.shields.io/badge/postgresql-15+-blue?logo=postgresql&logoColor=white" alt="PostgreSQL" />
<img src="https://img.shields.io/badge/docker-compose-blue?logo=docker&logoColor=white" alt="Docker" />
<img src="https://img.shields.io/badge/openai-integrated-black?logo=openai&logoColor=white" alt="OpenAI API" />
<img src="https://img.shields.io/badge/status-active_development-ff69b4" alt="Status" />



[Quick Start](#quick-start) · [Arquitetura](#arquitetura) · [API Reference](#api-reference) · [Decisões de Design](#decisões-de-design) · [Roadmap](#roadmap)



| **Processamento** assíncrono de áudio | **Inteligência Artificial** Contextualizada | **Tool Calling** integrado |
| :--- | :--- | :--- |
| **Arquitetura** em camadas estritas | **Fluxo Bidirecional** de Mídia (TTS) | **PostgreSQL** source of truth |

</div>

---

> ⚠️ **Work in progress** — A funcionalidade central da API e as integrações com IA estão estáveis e funcionais. Atualizações de segurança (JWT) e modularização do frontend estão em desenvolvimento ativo. Veja o [Roadmap](#roadmap) para o que está por vir.



### O Problema

Criar um CRUD de despesas financeiras é fácil. Construir um sistema que elimine a fricção da entrada de dados não é.

A maioria dos aplicativos exige digitação manual tediosa. Este projeto subverte essa lógica: você envia um áudio ("Gastei 15 reais na farmácia"), a API transcreve, a IA extrai os dados estruturados (valor, categoria, descrição) e persiste no banco de dados relacional. Além de registrar, a API atua como um Consultor Financeiro, cruzando dados reais do banco para responder perguntas complexas do usuário e sintetizando as respostas de volta em áudio.

---

### Quick Start

**Pré-requisitos:** JDK 17+, Maven 3.8+, Docker

```bash
git clone [https://github.com/PedroHenrique1801/budgeting-core-api.git](https://github.com/PedroHenrique1801/budgeting-core-api.git)
cd budgeting-core-api
Inicie o PostgreSQL via Docker:Bashdocker compose up -d
Configure as variáveis de ambiente:Bashexport SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/budgeting
export SPRING_DATASOURCE_USERNAME=seu_usuario
export SPRING_DATASOURCE_PASSWORD=sua_senha
export OPENAI_API_KEY=sua_chave_aqui
Compile e rode:Bashmvn clean install
mvn spring-boot:run
A API estará disponível em http://localhost:8080.Nota: Certifique-se de que a sua chave da OpenAI possui fundos/créditos ativos para as funções de transcrição e chat.ArquiteturaEstrutura em camadas isoladas. A separação estrita de responsabilidades garante que a lógica de negócios não vaze para os controladores HTTP.Plaintext  HTTP Request (Áudio ou JSON)
       │
       ▼
┌─────────────────────────────────────────────┐
│  Controller Layer                           │
│  Lida com HTTP, Multipart form-data         │
│  TranscriptionController / TextToSpeech     │
└────────────────────┬────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────┐
│  Application / Domain Layer                 │
│  Regras de negócio, Integração com IA       │
│  Orquestra transcrição, Tool Calling e RAG  │
└────────────────────┬────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────┐
│  Infrastructure Layer                       │
│  Spring Data JPA — Queries otimizadas       │
│  Comunicação externa com APIs de LLM        │
└────────────────────┬────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────┐
│  PostgreSQL 15+ (Docker)                    │
│  Source of truth — Nunca exposto direto    │
└─────────────────────────────────────────────┘
API ReferenceTransações & Inteligência ArtificialMétodoRotaDescriçãoStatusPOST/transactions/slRecebe arquivo .mp3, transcreve e salva a despesa200POST/api/synthesizeConverte texto em áudio (Text-to-Speech)200POST/transactions/advisorChat IA que consulta o banco para análises reais200Consultas de DadosMétodoRotaDescriçãoStatusGET/transactions/{category}Lista todas as transações de uma categoria200GET/transactions/sum/{category}Retorna a soma total gasta em uma categoria200Decisões de DesignCada padrão aplicado aqui resolve um problema real de engenharia.Inteligência Artificial Contextualizada (Tool Calling): A IA não "chuta" valores. O Advisor tem permissão controlada para consultar métodos do Spring Boot, acessar o banco de dados e usar o contexto matemático exato do usuário para responder perguntas.Processamento de Áudio Multipart: Manipulação direta de fluxos de áudio via multipart/form-data, garantindo que os binários não sobrecarreguem a memória do servidor antes do envio para a API de transcrição.Arquitetura Orientada a Domínio (DDD Layout): Código organizado por responsabilidades claras (application, domain, infrastructure). Se a provedora da IA mudar amanhã, o Domain permanece intocado.Fluxo Bidirecional de Mídia: A API não apenas consome áudio, ela também o gera. O TextToSpeechController garante uma experiência de usuário conversacional e fluida.Tech StackComponenteTecnologiaPor quê?LinguagemJava 17 LTSOrientação a objetos robusta, forte ecossistema.FrameworkSpring Boot 3.xPadrão da indústria, injeção de dependência nativa.Integração IASpring AI / OpenAITranscrição (Whisper), TTS e LLMs nativos no ecossistema Spring.PersistênciaSpring Data JPAAbstração limpa sobre JDBC e banco de dados.DatabasePostgreSQL 15+Relacional, confiável, pronto para produção.ContainersDocker + ComposeAmbientes reprodutíveis sem complexidade.Project StructurePlaintextsrc/
└── main/
    └── java/
        └── com/example/budgeting/
            ├── application/      # Regras de negócio, serviços e casos de uso
            ├── domain/           # Entidades principais e objetos de valor
            └── infrastructure/   # Implementações técnicas e Controladores
                ├── BudgetingApplication.java
                ├── ChatClientController.java
                ├── ChatModelController.java
                ├── SynthesizeRequest.java
                ├── TextToSpeechController.java
                └── TranscriptionController.java