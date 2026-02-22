# Calm Tasks - To-Do List API 📝🌿

API backend e aplicação web para gerenciamento de tarefas pessoais ("Calm Tasks"), desenvolvida para fornecer um controle eficiente de afazeres com **segurança de ponta** e uma interface minimalista.

O projeto utiliza **Spring Boot 3.4.3** para uma arquitetura robusta e **Spring Security 6** com **JWT (JSON Web Tokens)** assinado por **chaves RSA**, garantindo autenticação segura via **Cookies HttpOnly**. Além da API REST, o projeto conta com um frontend renderizado no servidor usando **Thymeleaf**.

---

## 📌 Índice

- [Funcionalidades](#-funcionalidades)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Configuração do Ambiente](#️-configuração-do-ambiente)
- [Modelagem do Banco de Dados](#-modelagem-do-banco-de-dados)
- [Documentação da API (Endpoints)](#️-documentação-da-api-endpoints)
- [Páginas Web (Frontend)](#-páginas-web-frontend)

---

## ✨ Funcionalidades

A aplicação implementa um ciclo completo de gerenciamento de tarefas aliado a um sistema de segurança rigoroso.

- ✅ **Frontend Integrado**: Interface visual completa ("Calm Tasks") usando Thymeleaf, TailwindCSS e Lucide Icons.
- ✅ **Autenticação JWT com RSA**: Login seguro que retorna um token JWT assinado por uma chave privada RSA de 2048 bits, armazenado de forma segura em um **Cookie HttpOnly**.
- ✅ **Gestão de Tarefas**: Criação, listagem e atualização de tarefas vinculadas exclusivamente ao usuário autenticado.
- ✅ **Relacionamento entre Entidades**: Cada tarefa é obrigatoriamente vinculada a um usuário dono.
- ✅ **Tratamento de Exceções**: Retornos padronizados e amigáveis para erros de validação, não autorização e recursos não encontrados.
- ✅ **Persistência em MySQL**: Banco de dados relacional para armazenamento seguro dos dados.

---

## 🚀 Tecnologias Utilizadas

**Backend:**
- **Java 17** – Linguagem principal  
- **Spring Boot 3.4.3** – Framework base  
- **Spring Security 6** – Proteção de rotas e autenticação JWT via Cookies  
- **Spring Data JPA** – Persistência e comunicação com o banco  
- **JWT (Nimbus JOSE + JWT)** – Geração e validação de tokens RSA  
- **MySQL** – Banco de dados relacional  
- **Lombok** – Redução de código boilerplate  

**Frontend:**
- **Thymeleaf** – Template engine para renderização SSR  
- **TailwindCSS** – Estilização utilitária (via CDN)  
- **Lucide Icons** – Ícones minimalistas  

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
git clone [https://github.com/seu-usuario/todolist-api.git](https://github.com/seu-usuario/todolist-api.git)
cd todolist-api
```

### 2️⃣ Geração das Chaves RSA (Obrigatório)

O projeto utiliza criptografia assimétrica. As chaves devem ser geradas através do git bash no diretório `src/main/resources` para que o Spring as reconheça no classpath.

Abra o Git Bash nessa pasta e execute um comando por vez:

🔑 **Gerar a chave privada:**

```bash
openssl genrsa -out app.key.pem 2048
```

🔄 **Converter para o formato PKCS#8** (Necessário para o Spring Security):

```bash
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt \
-in app.key.pem -out app.key.pem.tmp && mv app.key.pem.tmp app.key.pem
```

🔓 **Gerar a chave pública:**

```bash
openssl rsa -in app.key.pem -pubout -out app.pub.pem
```

> ⚠️ **Atenção:** Os arquivos `.pem` estão listados no `.gitignore` e não devem ser enviados para o repositório remoto. 🚫🔐

---

## 🗄️ Modelagem do Banco de Dados

Crie o banco de dados e as tabelas utilizando o script abaixo:

```sql
CREATE DATABASE TO_DO_LIST DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE TO_DO_LIST;

CREATE TABLE DB_USER (
    USER_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    USER_NAME VARCHAR(100) NOT NULL,
    USER_EMAIL VARCHAR(128) NOT NULL UNIQUE,
    USER_PASSWORD VARCHAR(255) NOT NULL
);

CREATE TABLE DB_TASK (
    TASK_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    TASK_DESCRIPTION VARCHAR(128),
    TASK_STATUS VARCHAR(16) NOT NULL,
    TASK_TITLE VARCHAR(128) NOT NULL,
    TASK_USER_ID BIGINT NOT NULL,
    CONSTRAINT fk_id_user 
        FOREIGN KEY (TASK_USER_ID) 
        REFERENCES DB_USER(USER_ID) 
        ON DELETE CASCADE
);
```

### 🔧 Configuração das Credenciais

Edite o arquivo `src/main/resources/application.properties` com suas credenciais do MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/to_do_list
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

---

## 🕹️ Documentação da API (Endpoints REST)

### 🔐 Endpoints de Autenticação e Usuário

#### 1️⃣ Registrar um Novo Usuário
Cria uma nova conta de usuário no sistema.

* **Endpoint:** `/user`
* **Método:** `POST`
* **Acesso:** Público

**Corpo da Requisição:**
```json
{
  "name": "Seu Nome",
  "email": "usuario@email.com",
  "password": "suaSenhaSegura"
}
```

**Respostas:**
* **`201 Created`** – Usuário registrado com sucesso.
* **`409 Conflict`** – E-mail já está em uso.

#### 2️⃣ Autenticar um Usuário (Login)
Autentica o usuário, retorna o token JWT e injeta automaticamente um Cookie HttpOnly no navegador.

* **Endpoint:** `/auth/login`
* **Método:** `POST`
* **Acesso:** Público

**Corpo da Requisição:**
```json
{
  "email": "usuario@email.com",
  "password": "suaSenhaSegura"
}
```

**Respostas:**
* **`200 OK`** – Sucesso (Retorna o JWT e define o Cookie token).
* **`401 Unauthorized`** – Credenciais inválidas (Bad Credentials).

#### 3️⃣ Sair da Conta (Logout)
Invalida a sessão excluindo o Cookie de autenticação.

* **Endpoint:** `/auth/logout`
* **Método:** `POST`
* **Acesso:** Privado

#### 4️⃣ Obter Perfil Logado
Retorna os dados do utilizador autenticado no momento.

* **Endpoint:** `/user/me`
* **Método:** `GET`
* **Acesso:** Privado

---

### 📝 Endpoints de Tarefas

Todas as rotas abaixo são protegidas e exigem autenticação prévia (Cookie JWT ativo). Os status válidos para as tarefas são: `TODO`, `IN_PROGRESS` e `COMPLETED`.

#### 1. Criar uma Nova Tarefa
Cria uma tarefa vinculada automaticamente ao utilizador autenticado.

* **Endpoint:** `/tasks`
* **Método:** `POST`

**Corpo da Requisição:**
```json
{
  "taskTitle": "Estudar Spring Boot",
  "taskDescription": "Finalizar o módulo de segurança",
  "taskStatus": "TODO"
}
```

**Respostas:**
* **`200 OK`** – Tarefa criada com sucesso.
* **`400 Bad Request`** – Dados inválidos (ex: título em branco).

#### 2. Listar Todas as Tarefas
Retorna todas as tarefas pertencentes ao utilizador logado.

* **Endpoint:** `/tasks`
* **Método:** `GET`

**Respostas:**
* **`200 OK`** – Retorna a lista de tarefas em formato JSON.

#### 3. Atualizar uma Tarefa
Permite alterar o título, descrição ou status de uma tarefa existente.

* **Endpoint:** `/tasks/{id}`
* **Método:** `PUT`

**Corpo da Requisição:**
```json
{
  "taskTitle": "Estudar Spring Boot e DTOs",
  "taskDescription": "Revisar a implementação de records",
  "taskStatus": "COMPLETED"
}
```

**Respostas:**
* **`200 OK`** – Tarefa atualizada com sucesso.
* **`403 Forbidden`** – A tarefa não pertence ao utilizador autenticado.
* **`404 Not Found`** – Tarefa não encontrada.

#### 4. Excluir uma Tarefa
Remove uma tarefa existente do sistema. O utilizador apenas pode excluir as suas próprias tarefas.

* **Endpoint:** `/tasks/{id}`
* **Método:** `DELETE`

**Parâmetros de Path:**
| Parâmetro | Descrição |
| :--- | :--- |
| `id` | ID numérico da tarefa a ser excluída |

**Respostas:**
* **`200 OK`** (ou **`204 No Content`**) – Tarefa excluída com sucesso.
* **`403 Forbidden`** – A tarefa não pertence ao utilizador autenticado.
* **`404 Not Found`** – Tarefa não encontrada.

---

## 🎨 Páginas Web (Frontend)

O projeto também serve páginas HTML renderizadas diretamente pelo Spring MVC (Thymeleaf). O controlo das views é feito pelo `LoginViewController`:

* `GET /` - Página inicial de apresentação do Calm Tasks (Landing Page).
* `GET /login` - Ecrã de autenticação do utilizador.
* `GET /register` - Ecrã de criação de nova conta.
* `GET /tasks-view` - Dashboard principal onde a lista de tarefas é exibida e gerida (Requer Autenticação).
* `GET /profile` - Ecrã de visualização do perfil do utilizador e suas conquistas/estatísticas (Requer Autenticação).

---

## ⚠️ Tratamento de Erros

A API utiliza um `RestExceptionHandler` para padronizar as respostas de erro por meio da classe `ApiError`.

**Padrão de resposta de Erro:**
```json
{
  "timestamp": "22-02-2026 15:30:00",
  "code": 400,
  "status": "BAD_REQUEST",
  "message": "Validation has failed for one or more fields",
  "error": "taskTitle: Title is required"
}
 ```



