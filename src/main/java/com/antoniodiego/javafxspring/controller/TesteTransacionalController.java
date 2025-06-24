package com.antoniodiego.javafxspring.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ProgressIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.antoniodiego.javafxspring.model.TarefaComposta;
import com.antoniodiego.javafxspring.service.TarefaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TesteTransacionalController {
    private static final Logger log = LoggerFactory.getLogger(TesteTransacionalController.class);

    @FXML
    private TextField idField;
    @FXML
    private TextField tituloField;
    @FXML
    private TextField descricaoField;
    @FXML
    private Button salvarButton;
    @FXML
    private Button buscarButton;
    @FXML
    private Button atualizarButton;
    @FXML
    private Button removerButton;
    @FXML
    private TextArea resultadoArea;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label statusLabel;

    private final TarefaService tarefaService;

    @Autowired
    public TesteTransacionalController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
        System.out.println("TarefaService class: " + tarefaService.getClass());
    }

    @FXML
    public void initialize() {
        progressIndicator.setVisible(false);
        statusLabel.setText("");
    }

    @FXML
    private void onSalvar() {
        progressIndicator.setVisible(true);
        statusLabel.setText("");
        TarefaComposta tarefa = new TarefaComposta();
        tarefa.setTitulo(tituloField.getText());
        tarefa.setComentario(descricaoField.getText());
        try {
            tarefaService.salvarOuAtualizar(tarefa);
            resultadoArea.setText("Tarefa salva com sucesso! ID: " + tarefa.getId());
            statusLabel.setText("Salvo");
        } catch (Exception e) {
            resultadoArea.setText("Erro ao salvar: " + e.getMessage());
            log.error("Erro ao salvar tarefa", e);
            statusLabel.setText("Erro");
        } finally {
            progressIndicator.setVisible(false);
        }
    }

    @FXML
    private void onBuscar() {
        progressIndicator.setVisible(true);
        statusLabel.setText("");
        javafx.concurrent.Task<Void> task = new javafx.concurrent.Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Long id = Long.parseLong(idField.getText());
                    TarefaComposta tarefa = (TarefaComposta) tarefaService.buscarTarefaCompostaPorId(id);
//                    Tarefa tarefa = tarefaService.buscarTarefaPorId()(id);
                    javafx.application.Platform.runLater(() -> {
                        if (tarefa != null) {
                            resultadoArea.setText("Tarefa encontrada: " + tarefa.getTitulo());
                            tituloField.setText(tarefa.getTitulo());
                            descricaoField.setText(tarefa.getComentario());
                            statusLabel.setText("Encontrado");
                        } else {
                            resultadoArea.setText("Tarefa não encontrada");
                            statusLabel.setText("Não encontrado");
                        }
                    });
                } catch (Exception e) {
                    javafx.application.Platform.runLater(() -> {
                        resultadoArea.setText("Erro ao buscar: " + e.getMessage());
                        log.error("Erro ao buscar tarefa", e);
                        statusLabel.setText("Erro");
                    });
                } finally {
                    javafx.application.Platform.runLater(() -> progressIndicator.setVisible(false));
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML
    private void onAtualizar() {
        progressIndicator.setVisible(true);
        statusLabel.setText("");
        javafx.concurrent.Task<Void> task = new javafx.concurrent.Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Long id = Long.parseLong(idField.getText());
                    TarefaComposta tarefa = (TarefaComposta) tarefaService.buscarTarefaCompostaPorId(id);
//                    Tarefa tarefa = tarefaService.buscarTarefaPorId()(id);
                    if (tarefa != null) {
                        tarefa.setTitulo(tituloField.getText());
                        tarefa.setComentario(descricaoField.getText());
                        tarefaService.salvar(tarefa);
                        javafx.application.Platform.runLater(() -> {
                            resultadoArea.setText("Tarefa atualizada com sucesso!");
                            statusLabel.setText("Atualizado");
                        });
                    } else {
                        javafx.application.Platform.runLater(() -> {
                            resultadoArea.setText("Tarefa não encontrada para atualizar");
                            statusLabel.setText("Não encontrado");
                        });
                    }
                } catch (Exception e) {
                    javafx.application.Platform.runLater(() -> {
                        resultadoArea.setText("Erro ao atualizar: " + e.getMessage());
                        log.error("Erro ao atualizar tarefa", e);
                        statusLabel.setText("Erro");
                    });
                } finally {
                    javafx.application.Platform.runLater(() -> progressIndicator.setVisible(false));
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML
    private void onRemover() {
        progressIndicator.setVisible(true);
        statusLabel.setText("");
        javafx.concurrent.Task<Void> task = new javafx.concurrent.Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Long id = Long.parseLong(idField.getText());
                    TarefaComposta tarefa = (TarefaComposta) tarefaService.buscarTarefaCompostaPorId(id);
//                    Tarefa tarefa = tarefaService.buscarTarefaPorId()(id);
                    if (tarefa != null) {
                        tarefaService.deletar(tarefa.getId());
                        javafx.application.Platform.runLater(() -> {
                            resultadoArea.setText("Tarefa removida com sucesso!");
                            statusLabel.setText("Removido");
                        });
                    } else {
                        javafx.application.Platform.runLater(() -> {
                            resultadoArea.setText("Tarefa não encontrada para remover");
                            statusLabel.setText("Não encontrado");
                        });
                    }
                } catch (Exception e) {
                    javafx.application.Platform.runLater(() -> {
                        resultadoArea.setText("Erro ao remover: " + e.getMessage());
                        log.error("Erro ao remover tarefa", e);
                        statusLabel.setText("Erro");
                    });
                } finally {
                    javafx.application.Platform.runLater(() -> progressIndicator.setVisible(false));
                }
                return null;
            }
        };
        new Thread(task).start();
    }
}
