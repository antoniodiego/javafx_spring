package com.antoniodiego.javafxspring.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.antoniodiego.javafxspring.JavaFxSpringApplication;
import com.antoniodiego.javafxspring.model.Comentario;
import com.antoniodiego.javafxspring.model.TarefaComposta;
import com.antoniodiego.javafxspring.service.ComentarioService;
import com.antoniodiego.javafxspring.service.TarefaService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

@Component
public class TaskEditDialogController {
    private static final Logger log = LoggerFactory.getLogger(TaskEditDialogController.class);
    
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private CheckBox completedCheckbox;
    @FXML private DatePicker completionDatePicker;
    @FXML private CheckBox enableCompletionDate;
    @FXML private ComboBox<Integer> priorityComboBox;
    @FXML private DatePicker dueDatePicker;
    @FXML private ListView<Comentario> commentsListView;
    @FXML private Button addCommentButton;
    @FXML private Button editCommentButton;
    @FXML private Button deleteCommentButton;
    @FXML private Label idLabel;
    @FXML private Label prioridadeLabel;
    
    @Autowired
    private ComentarioService comentarioService;
    
    private TarefaComposta tarefa;
    private Dialog<ButtonType> dialog;
    private ObservableList<Comentario> comentarios = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        log.info("TaskEditDialogController inicializado");
        
        // Configurar combo de prioridade
        priorityComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        
        // Binding para habilitar/desabilitar datepicker de conclusão
        completionDatePicker.disableProperty().bind(enableCompletionDate.selectedProperty().not());
        
        // Configurar listview de comentários
        commentsListView.setItems(comentarios);
        commentsListView.setCellFactory(listView -> new CommentListCell());
        
        // Habilitar/desabilitar botões de editar/excluir conforme seleção
        editCommentButton.disableProperty().bind(
                commentsListView.getSelectionModel().selectedItemProperty().isNull());
        deleteCommentButton.disableProperty().bind(
                commentsListView.getSelectionModel().selectedItemProperty().isNull());
    }
    
    /**
     * Configura o diálogo para edição
     */
    public void setDialog(Dialog<ButtonType> dialog) {
        this.dialog = dialog;
    }
    
    /**
     * Configura a tarefa para edição
     */
    public void setTarefa(TarefaComposta tarefa) {
        this.tarefa = tarefa;
        
        // Preencher os campos com dados da tarefa
        titleField.setText(tarefa.getTitulo());
        
        if (tarefa.getComentario() != null) {
            descriptionArea.setText(tarefa.getComentario());
        }
        
        completedCheckbox.setSelected(tarefa.isConcluida());
        
        if (tarefa.getDataConclusao() != null) {
            enableCompletionDate.setSelected(true);
            completionDatePicker.setValue(tarefa.getDataConclusao().toLocalDate());
        } else {
            enableCompletionDate.setSelected(false);
        }
        
        priorityComboBox.setValue(tarefa.getPrioridade());
        
        if (tarefa.getDataFazer() != null) {
            dueDatePicker.setValue(tarefa.getDataFazer());
        }
        
        if (idLabel != null) {
            idLabel.setText(tarefa.getIdPers() != null ? tarefa.getIdPers().toString() : "-");
        }
        if (prioridadeLabel != null) {
            prioridadeLabel.setText(tarefa.getPrioridade() != null ? tarefa.getPrioridade().toString() : "-");
        }
        
        // Carregar comentários
        if (tarefa.getComentarios() != null) {
            comentarios.setAll(tarefa.getComentarios());
        }
    }
    
    /**
     * Aplica as alterações à tarefa
     */
    public void updateTarefa() {
        tarefa.setTitulo(titleField.getText());
        tarefa.setComentario(descriptionArea.getText());
        tarefa.setConcluida(completedCheckbox.isSelected());
        
        // Data de conclusão
        if (enableCompletionDate.isSelected() && completionDatePicker.getValue() != null) {
            LocalDateTime dataHoraConcl = LocalDateTime.of(
                    completionDatePicker.getValue(), 
                    LocalTime.now());
            tarefa.setDataConclusao(dataHoraConcl);
        } else {
            tarefa.setDataConclusao(null);
        }
        
        // Prioridade
        if (priorityComboBox.getValue() != null) {
            tarefa.setPrioridade(priorityComboBox.getValue());
        } else {
            tarefa.setPrioridade(3); // Prioridade padrão média
        }
        
        // Data para fazer
        if (dueDatePicker.getValue() != null) {
            tarefa.setDataFazer(dueDatePicker.getValue());
        } else {
            tarefa.setDataFazer(null);
        }
        
        // Comentários já são referências, não precisa atualizar
        
        // Salvar no banco de dados
        TarefaService tarefaService = JavaFxSpringApplication.getSpringContext().getBean(TarefaService.class);
        if (tarefaService == null) {
            log.error("TarefaService não disponível");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao salvar tarefa");
            alert.setContentText("Ocorreu um erro ao salvar a tarefa: TarefaService não disponível.");
            alert.showAndWait();
            return;
        }
        tarefaService.salvar(tarefa);
        log.info("Tarefa {} atualizada com sucesso", tarefa.getId());
    }
    
    /**
     * Abre diálogo para adicionar comentário
     */
    @FXML
    private void handleAddComment() {
        TextInputDialog commentDialog = new TextInputDialog();
        commentDialog.setTitle("Novo Comentário");
        commentDialog.setHeaderText("Adicionar um novo comentário");
        commentDialog.setContentText("Texto do comentário:");
        
        Optional<String> result = commentDialog.showAndWait();
        result.ifPresent(commentText -> {
            if (!commentText.trim().isEmpty()) {
                Comentario novoComentario = new Comentario();
                novoComentario.setTarefa(tarefa);
                novoComentario.setComentario(commentText);
                novoComentario.setDataComentario(LocalDate.now());
                novoComentario.setHora(LocalTime.now());
                
                // Adicionar à lista e à tarefa
                comentarios.add(novoComentario);
                if (tarefa.getComentarios() == null) {
                    tarefa.setComentarios(new ArrayList<>());
                }
                tarefa.getComentarios().add(novoComentario);
                
                // Persistir o comentário
                ComentarioService daoComentario = JavaFxSpringApplication.getSpringContext().getBean(ComentarioService.class);
                if (daoComentario == null) {
                    log.error("DAOComentario não disponível");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText("Erro ao adicionar comentário");
                    alert.setContentText("Ocorreu um erro ao adicionar o comentário: DAOComentario não disponível.");
                    alert.showAndWait();
                    return;
                }
                if (daoComentario != null) {
                    daoComentario.salvar(novoComentario);
                }
            }
        });
    }
    
    /**
     * Abre diálogo para editar comentário selecionado
     */
    @FXML
    private void handleEditComment() {
        Comentario comentarioSelecionado = commentsListView.getSelectionModel().getSelectedItem();
        if (comentarioSelecionado == null) return;
        
        TextInputDialog commentDialog = new TextInputDialog(comentarioSelecionado.getComentario());
        commentDialog.setTitle("Editar Comentário");
        commentDialog.setHeaderText("Editar comentário");
        commentDialog.setContentText("Texto do comentário:");
        
        Optional<String> result = commentDialog.showAndWait();
        result.ifPresent(commentText -> {
            if (!commentText.trim().isEmpty()) {
                comentarioSelecionado.setComentario(commentText);
                comentarioSelecionado.setDataComentario(LocalDate.now());
                comentarioSelecionado.setHora(LocalTime.now());
                
                // Atualizar view
                commentsListView.refresh();
                
                // Persistir alteração
                // if (daoComentario != null) {
                //     daoComentario.atualiza(comentarioSelecionado);
                // }
            }
        });
    }
    
    /**
     * Exclui o comentário selecionado
     */
    @FXML
    private void handleDeleteComment() {
        Comentario comentarioSelecionado = commentsListView.getSelectionModel().getSelectedItem();
        if (comentarioSelecionado == null) return;
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir Comentário");
        alert.setContentText("Tem certeza que deseja excluir este comentário?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Remover da lista e da tarefa
            comentarios.remove(comentarioSelecionado);
            tarefa.getComentarios().remove(comentarioSelecionado);
            
            // Remover do banco
    
        }
    }
    
    /**
     * Célula customizada para exibir comentários
     */
    private static class CommentListCell extends ListCell<Comentario> {
        
        @Override
        protected void updateItem(Comentario comentario, boolean empty) {
            super.updateItem(comentario, empty);
            
            if (empty || comentario == null) {
                setText(null);
                setGraphic(null);
                return;
            }
            
            String dataFormatada = "";
            if (comentario.getDataComentario() != null && comentario.getHora() != null) {
                dataFormatada = comentario.getDataComentario() + " " + comentario.getHora().format(DateTimeFormatter.ofPattern("HH:mm"));
            } else {
                dataFormatada = "Data desconhecida";
            }
            
            setText(dataFormatada + " - " + comentario.getComentario());
        }
    }
    
    /**
     * Método estático para abrir o diálogo de edição
     */
    public static boolean editTask(TarefaComposta tarefa) {
        try {
            System.out.println("Chamou editTask"); // Para depuração
            // Carregar FXML
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Editar Tarefa");
            // Configurar FXML e controller
            DialogPane dialogPane = FXMLLoader.load(
                    TaskEditDialogController.class.getResource("/fxml/TaskEditDialog.fxml"));
            dialog.setDialogPane(dialogPane);
            // NÃO adicionar botões aqui, pois já estão definidos no FXML
            // dialog.getDialogPane().getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
            // Obter controlador e configurar
            TaskEditDialogController controller = (TaskEditDialogController) dialogPane.getProperties().get("controller");
            controller.setDialog(dialog);
            controller.setTarefa(tarefa);
            // Mostrar diálogo e esperar resultado
            Optional<ButtonType> result = dialog.showAndWait();
            // Se usuário clicou em Aplicar, salvar alterações
            if (result.isPresent() && result.get() == ButtonType.APPLY) {
                controller.updateTarefa();
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("Erro ao abrir diálogo de edição", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao abrir editor");
            alert.setContentText("Ocorreu um erro ao abrir o editor de tarefas: " + e.getMessage());
            alert.showAndWait();
            return false;
        }
    }
}