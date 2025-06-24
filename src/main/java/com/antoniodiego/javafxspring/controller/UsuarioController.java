package com.antoniodiego.javafxspring.controller;

<<<<<<< HEAD:src/main/java/com/example/javafxspring/controller/UsuarioController.java
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.javafxspring.model.Usuario;
import com.example.javafxspring.service.UsuarioService;

=======
>>>>>>> 8bd250582c022b579889352c6761da569119342e:src/main/java/com/antoniodiego/javafxspring/controller/UsuarioController.java
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
<<<<<<< HEAD:src/main/java/com/example/javafxspring/controller/UsuarioController.java
=======
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.antoniodiego.javafxspring.model.Usuario;
import com.antoniodiego.javafxspring.service.UsuarioService;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
>>>>>>> 8bd250582c022b579889352c6761da569119342e:src/main/java/com/antoniodiego/javafxspring/controller/UsuarioController.java

@Component
public class UsuarioController implements Initializable {

    @Autowired
    private UsuarioService usuarioService;

    // Campos do formulário
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtIdade;
    @FXML private TextField txtBusca;    // Botões
    @FXML private Button btnSalvar;
    @FXML private Button btnAtualizar;
    @FXML private Button btnDeletar;
    @FXML private Button btnLimpar;
    @FXML private Button btnBuscar;
    
    // Botões de teste
    @FXML private Button btnTesteAtualizacao;
    @FXML private Button btnTesteRollback;
    @FXML private Button btnCriarDadosTeste;

    // Tabela
    @FXML private TableView<UsuarioTableModel> tabelaUsuarios;
    @FXML private TableColumn<UsuarioTableModel, Long> colId;
    @FXML private TableColumn<UsuarioTableModel, String> colNome;
    @FXML private TableColumn<UsuarioTableModel, String> colEmail;
    @FXML private TableColumn<UsuarioTableModel, Integer> colIdade;

    // Labels de status
    @FXML private Label lblStatus;
    @FXML private Label lblTotalUsuarios;

    private ObservableList<UsuarioTableModel> dadosTabela = FXCollections.observableArrayList();
    private Long idUsuarioSelecionado = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabela();
        carregarUsuarios();
        configurarEventos();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));

        tabelaUsuarios.setItems(dadosTabela);

        // Listener para seleção na tabela
        tabelaUsuarios.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    preencherFormulario(newSelection);
                }
            }
        );
    }    private void configurarEventos() {
        btnSalvar.setOnAction(e -> salvarUsuario());
        btnAtualizar.setOnAction(e -> atualizarUsuario());
        btnDeletar.setOnAction(e -> deletarUsuario());
        btnLimpar.setOnAction(e -> limparFormulario());
        btnBuscar.setOnAction(e -> buscarUsuarios());
        
        // Eventos dos botões de teste
        btnTesteAtualizacao.setOnAction(e -> testeAtualizacaoEmLote());
        btnTesteRollback.setOnAction(e -> testeRollback());
        btnCriarDadosTeste.setOnAction(e -> criarDadosDeTeste());
    }

    @FXML
    private void salvarUsuario() {
        try {
            if (!validarCampos()) return;

            Usuario usuario = new Usuario();
            usuario.setNome(txtNome.getText().trim());
            usuario.setEmail(txtEmail.getText().trim());
            usuario.setIdade(Integer.parseInt(txtIdade.getText().trim()));

            usuarioService.salvar(usuario);
            
            mostrarStatus("Usuário salvo com sucesso!", false);
            limparFormulario();
            carregarUsuarios();
            
        } catch (Exception e) {
            mostrarStatus("Erro ao salvar usuário: " + e.getMessage(), true);
        }
    }

    @FXML
    private void atualizarUsuario() {
        try {
            if (idUsuarioSelecionado == null) {
                mostrarStatus("Selecione um usuário para atualizar!", true);
                return;
            }

            if (!validarCampos()) return;

            Usuario usuario = new Usuario();
            usuario.setNome(txtNome.getText().trim());
            usuario.setEmail(txtEmail.getText().trim());
            usuario.setIdade(Integer.valueOf(txtIdade.getText().trim()));

            usuarioService.atualizar(idUsuarioSelecionado, usuario);
            
            mostrarStatus("Usuário atualizado com sucesso!", false);
            limparFormulario();
            carregarUsuarios();
            
        } catch (Exception e) {
            mostrarStatus("Erro ao atualizar usuário: " + e.getMessage(), true);
        }
    }

    @FXML
    private void deletarUsuario() {
        try {
            if (idUsuarioSelecionado == null) {
                mostrarStatus("Selecione um usuário para deletar!", true);
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Exclusão");
            alert.setHeaderText("Deletar Usuário");
            alert.setContentText("Tem certeza que deseja deletar o usuário selecionado?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                usuarioService.deletar(idUsuarioSelecionado);
                mostrarStatus("Usuário deletado com sucesso!", false);
                limparFormulario();
                carregarUsuarios();
            }
            
        } catch (Exception e) {
            mostrarStatus("Erro ao deletar usuário: " + e.getMessage(), true);
        }
    }

    @FXML
    private void buscarUsuarios() {
        String termoBusca = txtBusca.getText().trim();
        
        if (termoBusca.isEmpty()) {
            carregarUsuarios();
            return;
        }

        try {
            List<Usuario> usuarios = usuarioService.buscarPorNome(termoBusca);
            atualizarTabela(usuarios);
            mostrarStatus("Busca realizada. " + usuarios.size() + " usuário(s) encontrado(s).", false);
        } catch (Exception e) {
            mostrarStatus("Erro na busca: " + e.getMessage(), true);
        }
    }

    private void carregarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.buscarTodos();
            atualizarTabela(usuarios);
            lblTotalUsuarios.setText("Total: " + usuarios.size() + " usuário(s)");
        } catch (Exception e) {
            mostrarStatus("Erro ao carregar usuários: " + e.getMessage(), true);
        }
    }

    private void atualizarTabela(List<Usuario> usuarios) {
        dadosTabela.clear();
        for (Usuario usuario : usuarios) {
            dadosTabela.add(new UsuarioTableModel(usuario));
        }
    }

    private void preencherFormulario(UsuarioTableModel usuarioModel) {
        idUsuarioSelecionado = usuarioModel.getId();
        txtNome.setText(usuarioModel.getNome());
        txtEmail.setText(usuarioModel.getEmail());
        txtIdade.setText(usuarioModel.getIdade().toString());
    }

    @FXML
    private void limparFormulario() {
        txtNome.clear();
        txtEmail.clear();
        txtIdade.clear();
        idUsuarioSelecionado = null;
        tabelaUsuarios.getSelectionModel().clearSelection();
        mostrarStatus("Formulário limpo.", false);
    }

    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty()) {
            mostrarStatus("Nome é obrigatório!", true);
            txtNome.requestFocus();
            return false;
        }

        if (txtEmail.getText().trim().isEmpty()) {
            mostrarStatus("Email é obrigatório!", true);
            txtEmail.requestFocus();
            return false;
        }

        try {
            Integer.parseInt(txtIdade.getText().trim());
        } catch (NumberFormatException e) {
            mostrarStatus("Idade deve ser um número válido!", true);
            txtIdade.requestFocus();
            return false;
        }

        return true;
    }

    private void mostrarStatus(String mensagem, boolean isError) {
        lblStatus.setText(mensagem);
        if (isError) {
            lblStatus.setStyle("-fx-text-fill: red;");
        } else {
            lblStatus.setStyle("-fx-text-fill: green;");
        }
    }

    // Classe modelo para a tabela
    public static class UsuarioTableModel {
        private final SimpleLongProperty id;
        private final SimpleStringProperty nome;
        private final SimpleStringProperty email;
        private final SimpleIntegerProperty idade;

        public UsuarioTableModel(Usuario usuario) {
            this.id = new SimpleLongProperty(usuario.getId());
            this.nome = new SimpleStringProperty(usuario.getNome());
            this.email = new SimpleStringProperty(usuario.getEmail());
            this.idade = new SimpleIntegerProperty(usuario.getIdade());
        }        public Long getId() { return id.get(); }
        public String getNome() { return nome.get(); }
        public String getEmail() { return email.get(); }
        public Integer getIdade() { return idade.get(); }
    }

    // ==================== MÉTODOS DE TESTE DE TRANSAÇÕES ====================

    /**
     * Teste de atualização em lote com transação
     */
    @FXML
    private void testeAtualizacaoEmLote() {
        try {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Teste de Atualização em Lote");
            confirmAlert.setHeaderText("Teste de Transação");
            confirmAlert.setContentText("Este teste irá atualizar a idade de todos os usuários adicionando +1 ano.\nContinuar?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                
                List<Usuario> usuarios = usuarioService.buscarTodos();
                int quantidadeAtualizada = 0;
                
                for (Usuario usuario : usuarios) {
                    usuario.setIdade(usuario.getIdade() + 1);
                    usuarioService.atualizar(usuario.getId(), usuario);
                    quantidadeAtualizada++;
                }
                
                carregarUsuarios();
                mostrarStatus("✅ Teste concluído! " + quantidadeAtualizada + " usuários atualizados com sucesso.", false);
                
                // Mostra informações da transação
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Resultado do Teste");
                infoAlert.setHeaderText("Transação Executada com Sucesso");
                infoAlert.setContentText(
                    "📊 Detalhes da operação:\n\n" +
                    "• Usuários processados: " + quantidadeAtualizada + "\n" +
                    "• Operação: Atualização em lote\n" +
                    "• Status: Todas as atualizações foram commitadas\n" +
                    "• Transação: @Transactional funcionando corretamente"
                );
                infoAlert.showAndWait();
            }
            
        } catch (Exception e) {
            mostrarStatus("❌ Erro no teste de atualização: " + e.getMessage(), true);
            
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erro na Transação");
            errorAlert.setHeaderText("Falha na Atualização em Lote");
            errorAlert.setContentText("Erro: " + e.getMessage() + "\n\nA transação foi revertida automaticamente.");
            errorAlert.showAndWait();
        }
    }

    /**
     * Teste de rollback de transação
     */
    @FXML
    private void testeRollback() {
        try {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Teste de Rollback");
            confirmAlert.setHeaderText("Teste de Falha de Transação");
            confirmAlert.setContentText(
                "Este teste irá tentar inserir usuários com email duplicado.\n" +
                "A transação deve falhar e fazer rollback.\nContinuar?"
            );

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                
                long usuariosAntes = usuarioService.contarUsuarios();
                
                // Cria lista com emails duplicados para forçar erro
                java.util.List<Usuario> usuariosTeste = java.util.Arrays.asList(
                    new Usuario("Teste Rollback 1", "teste.rollback@email.com", 25),
                    new Usuario("Teste Rollback 2", "teste.rollback2@email.com", 30),
                    new Usuario("Teste Rollback 3", "teste.rollback@email.com", 35) // Email duplicado!
                );

                try {
                    usuarioService.salvarMultiplosUsuarios(usuariosTeste);
                    
                    // Se chegou aqui, algo deu errado no teste
                    mostrarStatus("⚠️ Teste falhou: Era esperado um erro!", true);
                    
                } catch (RuntimeException e) {
                    // Erro esperado - rollback funcionou
                    long usuariosDepois = usuarioService.contarUsuarios();
                    carregarUsuarios();
                    
                    mostrarStatus("✅ Teste de rollback executado com sucesso!", false);
                    
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Teste de Rollback Bem Sucedido");
                    successAlert.setHeaderText("Transação Revertida Corretamente");
                    successAlert.setContentText(
                        "🔄 Resultado do teste:\n\n" +
                        "• Usuários antes: " + usuariosAntes + "\n" +
                        "• Usuários depois: " + usuariosDepois + "\n" +
                        "• Diferença: " + (usuariosDepois - usuariosAntes) + "\n\n" +
                        "✅ Rollback funcionou: Nenhum usuário foi inserido!\n" +
                        "🎯 @Transactional detectou o erro e reverteu todas as operações."
                    );
                    successAlert.showAndWait();
                }
            }
            
        } catch (Exception e) {
            mostrarStatus("❌ Erro no teste de rollback: " + e.getMessage(), true);
        }
    }

    /**
     * Cria dados de teste para demonstrações
     */
    @FXML
    private void criarDadosDeTeste() {
        try {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Criar Dados de Teste");
            confirmAlert.setHeaderText("Inserir Usuários de Exemplo");
            confirmAlert.setContentText("Deseja adicionar 5 usuários de exemplo para testes?\n(Emails únicos serão gerados automaticamente)");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                
                long timestamp = System.currentTimeMillis();
                
                java.util.List<Usuario> usuariosTeste = java.util.Arrays.asList(
                    new Usuario("Alice Silva", "alice.silva." + timestamp + "@teste.com", 28),
                    new Usuario("Bruno Santos", "bruno.santos." + timestamp + "@teste.com", 32),
                    new Usuario("Carla Oliveira", "carla.oliveira." + timestamp + "@teste.com", 26),
                    new Usuario("Diego Costa", "diego.costa." + timestamp + "@teste.com", 29),
                    new Usuario("Elena Ferreira", "elena.ferreira." + timestamp + "@teste.com", 31)
                );

                int inseridos = 0;
                for (Usuario usuario : usuariosTeste) {
                    usuarioService.salvar(usuario);
                    inseridos++;
                }
                
                carregarUsuarios();
                mostrarStatus("✅ " + inseridos + " usuários de teste criados com sucesso!", false);
                
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Dados de Teste Criados");
                infoAlert.setHeaderText("Usuários Inseridos com Sucesso");
                infoAlert.setContentText(
                    "📝 Usuários criados: " + inseridos + "\n\n" +
                    "Agora você pode testar:\n" +
                    "• Atualização em lote\n" +
                    "• Busca por nome\n" +
                    "• Edição individual\n" +
                    "• Exclusão de registros"
                );
                infoAlert.showAndWait();
            }
            
        } catch (Exception e) {
            mostrarStatus("❌ Erro ao criar dados de teste: " + e.getMessage(), true);
        }
    }
}
