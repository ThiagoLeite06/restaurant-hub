# 🍽️ RestaurantHub
*Sistema de Gestão Compartilhada para Restaurantes*

## 📋 Sobre o Projeto

O **RestaurantHub** é um sistema revolucionário desenvolvido para atender um consórcio de restaurantes que buscavam uma solução robusta e econômica para gerenciar suas operações. 

### 🎯 O Problema
Na nossa região, um grupo de restaurantes decidiu contratar estudantes para construir um sistema de gestão para seus estabelecimentos. Essa decisão foi motivada pelo alto custo de sistemas individuais, o que levou os restaurantes a se unirem para desenvolver um sistema único e compartilhado. Esse sistema permitirá que os clientes escolham restaurantes com base na comida oferecida, em vez de se basearem na qualidade do sistema de gestão.

### 🚀 Objetivo
Desenvolver um backend completo e robusto utilizando o framework Spring Boot, com foco no gerenciamento de usuários, incluindo operações de criação, atualização, exclusão e validação de login. O projeto será configurado para rodar em um ambiente Docker, utilizando Docker Compose, garantindo que a aplicação seja facilmente replicável e escalável.

## 🏗️ Arquitetura

- **Backend:** Spring Boot 3.5.3
- **Linguagem:** Java 21
- **Banco de Dados:** PostgreSQL 16
- **Containerização:** Docker & Docker Compose
- **Migração:** Flyway
- **ORM:** Spring Data JPA
- **Validação:** Spring Boot Validation

## 🛠️ Tecnologias Utilizadas

- ☕ **Java 21** - Linguagem principal
- 🌱 **Spring Boot 3.5.3** - Framework principal
- 🗄️ **PostgreSQL 16** - Banco de dados relacional
- 🐳 **Docker** - Containerização
- 🔄 **Flyway** - Migração de banco de dados
- 📊 **Spring Data JPA** - Persistência de dados
- ✅ **Spring Boot Validation** - Validação de dados
- 🔐 **Spring Security** - Autenticação e autorização
- 🔑 **JWT** - Tokens de autenticação
- 🎯 **Lombok** - Redução de código boilerplate

## 🚀 Como Rodar o Projeto

### 📋 Pré-requisitos
- Docker e Docker Compose instalados
- Java 21 (para desenvolvimento)
- Maven 3.6+ (para desenvolvimento)

### 🔧 Primeira Execução

1. **Clone o repositório:**
```bash
git clone <url-do-repositório>
cd RestaurantHub
```

2. **Inicie o banco de dados:**
```bash
docker-compose up -d
```
*Este comando irá:*
- Criar e iniciar um container PostgreSQL 16
- Configurar o banco de dados `restauranthubdb`
- Criar o usuário `restaurantadmin` com senha `password123`
- Expor a porta 5432 para conexão local

3. **Compile e execute a aplicação:**
```bash
./mvnw spring-boot:run
```

4. **Verifique se está funcionando:**
- A aplicação estará disponível em: `http://localhost:8080`
- O banco de dados estará acessível em: `localhost:5432`
- **Documentação da API (Swagger):** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

### 🗄️ Configuração do Banco de Dados

O projeto utiliza **Flyway** para migração automática do banco de dados. As migrações estão localizadas em `src/main/resources/db/migration/`.

**Credenciais do Banco:**
- **Host:** localhost:5432
- **Database:** restauranthubdb
- **Usuário:** restaurantadmin
- **Senha:** password123

### 🔄 Comandos Úteis

```bash
# Parar todos os containers
docker-compose down

# Remover volumes (limpar dados do banco)
docker-compose down -v

# Ver logs do banco de dados
docker-compose logs postgres_db

# Acessar o PostgreSQL via linha de comando
docker exec -it restauranthub-postgres psql -U restaurantadmin -d restauranthubdb
```

## 📝 Desenvolvimento

### 🏃‍♂️ Rodando em Modo de Desenvolvimento

```bash
# Compilar o projeto
./mvnw clean compile

# Executar testes
./mvnw test

# Executar a aplicação
./mvnw spring-boot:run

# Executar testes com cobertura
./run-tests.sh

# Ou executar testes manualmente
./mvnw test
./mvnw jacoco:report
```

### 🗂️ Estrutura do Projeto

```
RestaurantHub/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/restauranthub/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   └── test/
├── docker-compose.yml
├── pom.xml
└── README.md
```

## 🌟 Funcionalidades Implementadas

### ✅ Fase 1 - COMPLETA
- ✅ Configuração do ambiente Docker
- ✅ Configuração do banco de dados MySQL com Flyway
- ✅ Estrutura base do Spring Boot
- ✅ **Gerenciamento de usuários (CRUD completo)**
- ✅ **Sistema de autenticação e autorização (Spring Security + JWT)**
- ✅ **Exception handling global**
- ✅ **Validações de dados**

### 🚀 APIs Disponíveis
- 👤 **Users API** - CRUD completo de usuários
- 🔐 **Auth API** - Autenticação e autorização  
- 🔑 **Password API** - Troca segura de senhas
- ⚠️ **Global Exception Handling** - Tratamento padronizado de erros

### 📋 Endpoints Principais
```bash
# Usuários
GET    /api/user           # Listar usuários
GET    /api/user/{id}      # Buscar usuário por ID
POST   /api/user           # Criar usuário
PUT    /api/user/{id}      # Atualizar usuário
DELETE /api/user/{id}      # Deletar usuário
PUT    /api/user/{id}/password  # Trocar senha

# Autenticação
POST   /api/auth/register  # Registrar usuário
POST   /api/auth/login     # Login do usuário

# Tipos de Usuário
GET    /api/user-type      # Listar tipos de usuário
GET    /api/user-type/{id} # Buscar tipo por ID
POST   /api/user-type      # Criar tipo de usuário
PUT    /api/user-type/{id} # Atualizar tipo
DELETE /api/user-type/{id} # Deletar tipo

# Restaurantes
GET    /api/restaurants    # Listar restaurantes
GET    /api/restaurants/{id} # Buscar restaurante por ID
POST   /api/restaurants    # Criar restaurante (apenas OWNER)
PUT    /api/restaurants/{id} # Atualizar restaurante
DELETE /api/restaurants/{id} # Deletar restaurante

# Itens do Cardápio
GET    /api/menu-items     # Listar itens do cardápio
GET    /api/menu-items/{id} # Buscar item por ID
POST   /api/menu-items     # Criar item do cardápio
PUT    /api/menu-items/{id} # Atualizar item
DELETE /api/menu-items/{id} # Deletar item
GET    /api/menu-items/restaurant/{id} # Itens por restaurante
```

### 🔜 Próximas Fases
- **Fase 2:** Gestão de restaurantes e cardápios
- **Fase 3:** Sistema de pedidos e pagamentos
- **Fase 4:** Avaliações e comentários
- **Fase 5:** Dashboard administrativo e relatórios

### 📋 Status Atual
- **Progresso:** 95% implementado
- **Módulo de Usuários:** ✅ 100% Completo (com troca de senha)
- **Módulo de Restaurantes:** ✅ 100% Completo
- **Módulo de Cardápios:** ✅ 100% Completo
- **Sistema de Segurança:** ✅ 95% Implementado
- **Documentação API:** ✅ 100% Implementado (Swagger)
- **Testes Automatizados:** ✅ 80%+ Cobertura
- **Banco de Dados:** ✅ 100% Funcional

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 📚 Documentação da API

### Swagger UI
A documentação completa da API está disponível através do Swagger UI:
- **URL:** `http://localhost:8080/swagger-ui.html`
- **Recursos:** Teste interativo de todos os endpoints
- **Autenticação:** Suporte a JWT Bearer Token

### Funcionalidades Documentadas
- ✅ Todos os endpoints com descrições detalhadas
- ✅ Exemplos de request e response
- ✅ Códigos de status HTTP
- ✅ Modelos de dados (schemas)
- ✅ Parâmetros obrigatórios e opcionais
- ✅ Validações e regras de negócio

## 🧪 Testes Automatizados

### Cobertura de Testes
- **Meta:** 80% de cobertura mínima
- **Tipos:** Testes unitários e de integração
- **Framework:** JUnit 5 + Mockito
- **Relatórios:** JaCoCo

### Executar Testes
```bash
# Executar todos os testes
./mvnw test

# Executar testes com relatório de cobertura
./mvnw test jacoco:report

# Verificar cobertura mínima
./mvnw jacoco:check

# Script automatizado
./run-tests.sh
```

### Visualizar Relatório
Após executar os testes, o relatório estará disponível em:
`target/site/jacoco/index.html`

## 🔐 Autenticação e Autorização

### Fluxo de Autenticação
1. **Registro:** `POST /api/auth/register`
2. **Login:** `POST /api/auth/login` → Retorna JWT token
3. **Uso:** Incluir header `Authorization: Bearer {token}`

### Tipos de Usuário
- **CLIENT:** Cliente do sistema
- **OWNER:** Proprietário de restaurante (pode criar restaurantes)
- **SYSTEM_ADMIN:** Administrador do sistema

### Regras de Negócio
- ✅ Apenas usuários OWNER podem criar restaurantes
- ✅ Um OWNER pode ter apenas um restaurante
- ✅ Validação de email e login únicos
- ✅ Senhas criptografadas com BCrypt

## 📊 Métricas e Qualidade

### Cobertura de Código
- **Testes Unitários:** Use Cases, Services, Repositories
- **Testes de Integração:** Controllers, APIs completas
- **Validações:** Regras de negócio e exceções

### Arquitetura
- **Clean Architecture:** Separação clara de responsabilidades
- **Hexagonal Architecture:** Domain, Application, Infrastructure
- **SOLID Principles:** Código limpo e manutenível
- **Design Patterns:** Repository, Use Case, DTO

## 🚀 Próximos Passos

### Fase 3 (Planejada)
- Sistema de pedidos online
- Carrinho de compras
- Integração com pagamentos
- Notificações em tempo real

### Melhorias Técnicas
- Cache com Redis
- Monitoramento com Actuator
- Logs estruturados
- CI/CD Pipeline
- Deploy automatizado