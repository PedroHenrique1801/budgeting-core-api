# Budgeting AI API

Sistema de controle financeiro fundamentado em Clean Architecture, utilizando Inteligência Artificial para processamento de linguagem natural e RAG (Retrieval-Augmented Generation).

## Arquitetura
A aplicação aplica os princípios de Clean Architecture para garantir o desacoplamento e o isolamento das regras de negócio:
* **Domain:** Entidades centrais e enums de domínio.
* **Application:** Casos de Uso (Use Cases) totalmente isolados de frameworks web e bancos de dados.
* **Infrastructure:** Controladores HTTP REST, integrações com APIs externas (OpenAI) e persistência de dados.

## Stack Tecnológico
* **Core:** Java 25, Spring Boot 3+
* **Inteligência Artificial:** Spring AI (Integração com modelos OpenAI Whisper, GPT e Text-to-Speech)
* **Persistência:** MySQL 9.6, Spring Data JPA, Hibernate
* **Infraestrutura:** Docker (Spring Boot Docker Compose)
* **Interface (Client):** HTML5, CSS3, JavaScript e Chart.js

## Quick Start
O ambiente de banco de dados é orquestrado de forma transparente via Docker Compose integrado nativamente ao Spring Boot.

1. Configure sua chave de API no arquivo `application.properties`:
   `spring.ai.openai.api-key=SUA_CHAVE_AQUI`

2. Certifique-se de que o Docker Desktop está em execução.

3. Execute a aplicação (a imagem do MySQL será baixada e instanciada automaticamente):
   `./gradlew bootRun`

A interface do painel administrativo e os endpoints estarão disponíveis em `http://localhost:8080`.