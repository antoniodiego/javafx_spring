# JavaFX + Spring Boot + Hibernate - Aplicação de Gerenciamento de Usuários

Esta é uma aplicação completa que demonstra a integração entre JavaFX, Spring Boot e Hibernate com uso de transações (`@Transactional`).

## 📋 Funcionalidades

- ✅ **CRUD completo** de usuários (Create, Read, Update, Delete)
- ✅ **Interface gráfica** com JavaFX
- ✅ **Injeção de dependência** com Spring
- ✅ **Persistência** com JPA/Hibernate
- ✅ **Transações** com `@Transactional`
- ✅ **Validações** de dados
- ✅ **Busca** por nome
- ✅ **Banco H2** em memória
- ✅ **Testes unitários** incluídos

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.1.0**
- **JavaFX 20.0.1**
- **Hibernate/JPA**
- **H2 Database**
- **Maven**
- **JUnit 5**

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/example/javafxspring/
│   │       ├── JavaFxSpringApplication.java    # Classe principal
│   │       ├── controller/
│   │       │   └── UsuarioController.java      # Controlador JavaFX
│   │       ├── model/
│   │       │   └── Usuario.java                # Entidade JPA
│   │       ├── repository/
│   │       │   └── UsuarioRepository.java      # Repositório JPA
│   │       └── service/
│   │           └── UsuarioService.java         # Serviço com @Transactional
│   └── resources/
│       ├── application.properties              # Configurações
│       ├── data.sql                           # Dados iniciais
│       └── usuario.fxml                       # Interface JavaFX
└── test/
    └── java/
        └── com/example/javafxspring/
            └── UsuarioServiceTest.java        # Testes unitários
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+

### Passos para execução:

1. **Clone ou baixe o projeto**

2. **Compile o projeto:**
   ```powershell
   mvn clean compile
   ```

3. **Execute a aplicação:**
   ```powershell
   mvn javafx:run
   ```

   **Ou alternativamente:**
   ```powershell
   mvn spring-boot:run
   ```

4. **Para executar os testes:**
   ```powershell
   mvn test
   ```

## 🎯 Como Usar a Aplicação

### Interface Principal
A aplicação abre uma janela com:
- **Formulário** para inserir/editar usuários
- **Tabela** com lista de usuários
- **Campo de busca** para filtrar por nome
- **Botões** para ações (Salvar, Atualizar, Deletar, Limpar)

### Operações Disponíveis

1. **Adicionar Usuário:**
   - Preencha Nome, Email e Idade
   - Clique em "Salvar"

2. **Editar Usuário:**
   - Clique em um usuário na tabela
   - Modifique os dados no formulário
   - Clique em "Atualizar"

3. **Deletar Usuário:**
   - Selecione um usuário na tabela
   - Clique em "Deletar"
   - Confirme a exclusão

4. **Buscar Usuários:**
   - Digite parte do nome no campo de busca
   - Clique em "Buscar"

5. **Limpar Formulário:**
   - Clique em "Limpar" para resetar o formulário

## 💾 Banco de Dados

- **Banco:** H2 (em memória)
- **URL:** `jdbc:h2:mem:javafxdb`
- **Console H2:** Habilitado em `/h2-console`
- **Dados iniciais:** Carregados automaticamente via `data.sql`

### Acessar Console H2
Se quiser visualizar o banco de dados:
1. Execute a aplicação
2. Abra o navegador em: `http://localhost:8080/h2-console`
3. Use as configurações do `application.properties`

## 🔄 Transações

O projeto demonstra o uso de `@Transactional` em diferentes cenários:

### Transações de Leitura (Read-Only)
```java
@Transactional(readOnly = true)
public List<Usuario> buscarTodos() {
    return usuarioRepository.findAllByOrderByNomeAsc();
}
```

### Transações de Escrita
```java
@Transactional
public Usuario salvar(Usuario usuario) {
    // Validações e salvamento
    return usuarioRepository.save(usuario);
}
```

### Transações com Rollback
```java
@Transactional
public void salvarMultiplosUsuarios(List<Usuario> usuarios) {
    for (Usuario usuario : usuarios) {
        salvar(usuario); // Se falhar, faz rollback de todos
    }
}
```

## 🧪 Testes

Os testes incluem:
- ✅ Salvamento de usuários
- ✅ Busca e filtros
- ✅ Validação de email único
- ✅ Teste de rollback transacional

Execute com: `mvn test`

## ⚙️ Configurações

### application.properties
```properties
# Banco H2
spring.datasource.url=jdbc:h2:mem:javafxdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Transações
spring.transaction.default-timeout=30
```

## 🔧 Personalização

### Adicionar Novos Campos
1. Modifique a entidade `Usuario.java`
2. Atualize o repositório se necessário
3. Modifique o serviço `UsuarioService.java`
4. Atualize a interface `usuario.fxml`
5. Modifique o controlador `UsuarioController.java`

### Mudar Banco de Dados
1. Adicione a dependência do banco no `pom.xml`
2. Atualize as configurações em `application.properties`
3. Ajuste o dialeto do Hibernate se necessário

## 📚 Aprendizados

Este projeto demonstra:
- **Integração** Spring + JavaFX
- **Injeção de Dependência** em controladores JavaFX
- **Gerenciamento de Transações** com Spring
- **Mapeamento Objeto-Relacional** com JPA/Hibernate
- **Padrão Repository** para acesso a dados
- **Separação de responsabilidades** (Model-View-Controller)

## 🚨 Troubleshooting

### Erro "No plugin found for prefix 'spring-boot'"
- Verifique se o `spring-boot-maven-plugin` está no `pom.xml`
- Execute: `mvn clean install`

### JavaFX não inicializa
- Verifique a versão do Java (17+)
- Certifique-se que as dependências JavaFX estão corretas

### Problemas de transação
- Verifique se `@Transactional` está na classe de serviço
- Confirme que `@EnableTransactionManagement` está habilitado (automático no Spring Boot)

## 📄 Licença

Este projeto é para fins educacionais e demonstrativos.
