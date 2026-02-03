# To-Do List API 📝

API backend para gerenciamento de tarefas pessoais, desenvolvida para fornecer um controle eficiente de afazeres com **segurança de ponta**.

O projeto utiliza **Spring Boot 3.4.3** para uma arquitetura robusta e **Spring Security 6** com **JWT (JSON Web Tokens)** assinado por **chaves RSA**, garantindo autenticação stateless e segura.

---

## 📌 Índice

- [Funcionalidades](#-funcionalidades)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Configuração do Ambiente](#️-configuração-do-ambiente)
- [Modelagem do Banco de Dados](#-modelagem-do-banco-de-dados)
- [Documentação da API (Endpoints)](#️-documentação-da-api-endpoints)

---

# 🕹️ Documentação da API (Endpoints)

A seguir estão detalhados os endpoints disponíveis na **To-Do List API**.

---

## 🔗 Navegação Rápida
- [Endpoints de Autenticação](#-endpoints-de-autenticação)
- [Endpoints de Tarefas](#-endpoints-de-tarefas)

---

## ✨ Funcionalidades

A API implementa um ciclo completo de gerenciamento de tarefas aliado a um sistema de segurança rigoroso.

- ✅ **Autenticação JWT com RSA**: Login seguro que retorna um token assinado por uma chave privada RSA de 2048 bits.
- ✅ **Gestão de Tarefas (CRUD)**: Criação, listagem, atualização e exclusão de tarefas vinculadas ao usuário.
- ✅ **Relacionamento entre Entidades**: Cada tarefa é obrigatoriamente vinculada a um usuário dono.
- ✅ **Segurança Stateless**: Nenhuma sessão é armazenada no servidor; a validação ocorre via token em cada requisição.
- ✅ **Persistência em MySQL**: Banco de dados relacional para armazenamento seguro dos dados.

---

## 🚀 Tecnologias Utilizadas

- **Java 17** – Linguagem principal  
- **Spring Boot 3.4.3** – Framework base  
- **Spring Security 6** – Proteção de rotas e autenticação JWT  
- **Spring Data JPA** – Persistência e comunicação com o banco  
- **JWT (Nimbus JOSE + JWT)** – Geração e validação de tokens  
- **MySQL** – Banco de dados relacional  
- **Lombok** – Redução de código boilerplate  

---

## 📋 Pré-requisitos

- JDK 17 ou superior  
- Maven 3.8+  
- MySQL Server 8.0+  
- OpenSSL (geralmente incluso no Git Bash)  
- Uma IDE (IntelliJ IDEA Ultimate recomendada)  

---

## ⚙️ Configuração do Ambiente

### 1️⃣ Clone o repositório

```bash
git clone https://github.com/seu-usuario/todolist-api.git
cd todolist-api
```

## 2️⃣ Geração das Chaves RSA (Obrigatório)

O projeto utiliza criptografia assimétrica. As chaves devem ser geradas através do git bash
no diretório `src/main/resources` para que o Spring as reconheça no **classpath**.

Abra o **Git Bash** nessa pasta e execute **um comando por vez**.

### 🔑 Gerar a chave privada

```bash
openssl genrsa -out app.key.pem 2048
```

### 🔄 Converter para o formato PKCS#8  
*(Necessário para o Spring Security)*

```bash
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt \
-in app.key.pem -out app.key.pem.tmp && mv app.key.pem.tmp app.key.pem
```

### 🔓 Gerar a chave pública

```bash
openssl rsa -in app.key.pem -pubout -out app.pub.pem
```

⚠️ **Atenção:**  

Os arquivos `.pem` estão listados no `.gitignore` e **não devem ser enviados para o GitHub**. 🚫🔐

---

## 🗄️ Modelagem do Banco de Dados

Crie o banco de dados e as tabelas utilizando o script abaixo:

```sql
CREATE DATABASE TO_DO_LIST;
USE TO_DO_LIST;

CREATE TABLE DB_USER (
    USER_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    USER_NAME VARCHAR(24) NOT NULL,
    USER_EMAIL VARCHAR(128) NOT NULL UNIQUE,
    USER_PASSWORD VARCHAR(255) NOT NULL
);

CREATE TABLE DB_TASK (
    TASK_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    TASK_DESCRIPTION VARCHAR(128),
    TASK_STATUS VARCHAR(16) NOT NULL,
    TASK_TITLE VARCHAR(128) NOT NULL,
    TASK_USER_ID BIGINT,
    CONSTRAINT fk_id_user 
        FOREIGN KEY (TASK_USER_ID) 
        REFERENCES DB_USER(USER_ID) 
        ON DELETE CASCADE
);
```

## 🔧 Configuração das Credenciais

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/to_do_list
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

## 🕹️ Documentação da API (Endpoints)

### 🔐 Endpoints de Autenticação

Rotas públicas para gestão de acesso e criação de contas.

---

#### 1️⃣ Registrar um Novo Usuário

Cria uma nova conta de usuário no sistema para que ele possa gerenciar suas próprias tarefas.

**Endpoint:** `/users`  
**Método:** `POST`

#### 📥 Requisição

| Atributo | Descrição |
|--------|-----------|
| URL | `/users` |
| Método | `POST` |
| Cabeçalhos | `Content-Type: application/json` |

**Corpo da Requisição (JSON):**
```json
{
  "name": "Seu Nome",
  "email": "usuario@email.com",
  "password": "suaSenhaSegura"
}
```

#### 📤 Respostas
- **201 Created** – Usuário registrado com sucesso.
- **409 Conflict** – E-mail já está em uso.

---

### 2️⃣ Autenticar um Usuário (Login)

Autentica o usuário e retorna um **Token JWT assinado com RSA** para acesso às rotas protegidas.

**Endpoint:** `/login`  
**Método:** `POST`

#### 📥 Requisição

**Corpo da Requisição (JSON):**
```json
{
  "email": "usuario@email.com",
  "password": "suaSenhaSegura"
}
```

#### 📤 Respostas
- **200 OK** – Autenticação bem-sucedida. Retorna o token JWT.
- **401 Unauthorized** – Credenciais inválidas.

---

## Endpoints de Tarefas

Todas as rotas abaixo são protegidas e exigem o cabeçalho:
`Authorization: Bearer <seu_token_jwt>`

### 1. Criar uma Nova Tarefa
Cria uma tarefa vinculada automaticamente ao usuário autenticado.

- **Endpoint:** `/tasks`
- **Método:** `POST`

#### 📥 Requisição
**Corpo da Requisição (CreateTaskDTO):**
```json
{
  "taskTitle": "Estudar Spring Boot",
  "taskDescription": "Finalizar o módulo de segurança",
  "taskStatus": "PENDING"
}
```
#### 📤 Respostas
- **`200 OK`** – Tarefa criada com sucesso.
- **`400 Bad Request`** – Dados inválidos (ex: título em branco).

---

### 2. Listar Todas as Tarefas
Retorna todas as tarefas pertencentes ao usuário logado.

- **Endpoint:** `/tasks`
- **Método:** `GET`

#### 📤 Respostas
- **`200 OK`** – Retorna a lista de tarefas.
- **`200 OK []`** – Retorna lista vazia caso não existam tarefas.

---

### 3. Atualizar uma Tarefa
Permite alterar o título, descrição ou status de uma tarefa existente.

- **Endpoint:** `/tasks/{id}`
- **Método:** `PUT`

#### 📥 Requisição
**Parâmetros de Path:**
| Parâmetro | Descrição |
| :--- | :--- |
| `id` | ID numérico da tarefa a ser atualizada |

**Corpo da Requisição (UpdateTaskDTO):**
```json
{
  "title": "Estudar Spring Boot e DTOs",
  "description": "Revisar a implementação de records",
  "status": "COMPLETED"
}
```
#### 📤 Respostas
- **`200 OK`** – Tarefa atualizada com sucesso.
- **`403 Forbidden`** – A tarefa não pertence ao usuário autenticado.
- **`404 Not Found`** – Tarefa não encontrada.

---

## ⚠️ Tratamento de Erros
A API utiliza um `RestExceptionHandler` para padronizar as respostas de erro por meio da classe `ApiError`.

**Possíveis erros:**
* **400 Bad Request** – Erros de validação nos campos do DTO.
* **403 Forbidden** – Tentativa de acesso a um recurso que não pertence ao usuário.
* **404 Not Found** – Recurso (Tarefa ou Usuário) não encontrado.
* **409 Conflict** – Violação de restrição (ex: e-mail duplicado).




