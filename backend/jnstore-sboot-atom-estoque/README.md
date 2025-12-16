# JNStore - Serviço de Estoque

Este é o serviço atômico de estoque para a aplicação JNStore.

## Descrição

O serviço é responsável por gerenciar o estoque de produtos, incluindo operações de CRUD (Criar, Ler, Atualizar, Deletar) para produtos.

## Configuração

Antes de iniciar a aplicação, é necessário configurar a conexão com o banco de dados. O erro `Failed to configure a DataSource` que você está vendo indica que a aplicação não conseguiu se conectar a um banco de dados.

**Observação:**
*   Altere `sua_senha` para a senha do seu banco de dados MySQL.
*   Certifique-se de que você tem um servidor MySQL rodando na `localhost:3306`.
*   O banco de dados `jnstore_estoque` será criado automaticamente se não existir.

## Como Executar

Após configurar o banco de dados, você pode executar a aplicação usando o Maven:

```bash
mvn spring-boot:run
```

## Documentação da API

A documentação da API está disponível via Swagger UI. Após iniciar a aplicação, acesse a seguinte URL no seu navegador:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
