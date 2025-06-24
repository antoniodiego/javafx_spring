package com.antoniodiego.javafxspring.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.antoniodiego.javafxspring.JavaFxSpringApplication;
import com.antoniodiego.javafxspring.model.TarefaComposta;
import com.antoniodiego.javafxspring.service.TarefaService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

@Component
public class MainWindowController {

    @FXML
    private VBox pilhasDeTarefasView; 

    @FXML
    private Label statusLabel;
    
   
    @Autowired
    private PilhasDeTarefasController pilhasDeTarefasController;
    
    @Autowired
    private ApplicationContext applicationContext;

    @FXML
    public void initialize() {
        try {
            System.out.println("MainWindowController inicializando...");
            
            if (pilhasDeTarefasView == null) {
                System.err.println("ERRO: pilhasDeTarefasView não foi injetado!");
                return;
            }
            
            if (pilhasDeTarefasController == null) {
                pilhasDeTarefasController = applicationContext.getBean(PilhasDeTarefasController.class);
                if (pilhasDeTarefasController == null) {
                    System.err.println("ERRO: Falha ao obter o bean PilhasDeTarefasController");
                    return;
                }
                
                VBox container = pilhasDeTarefasController.getEstanteContainer();
                if (container != null) {
                    pilhasDeTarefasView.getChildren().clear();
                    pilhasDeTarefasView.getChildren().add(container);
                }
            }
            
            // if (taskTreeViewController == null) {
            //     System.err.println("AVISO: TaskTreeViewController não foi injetado!");
            // }
            
            // Atualiza as pilhas de tarefas após um pequeno atraso para garantir que a UI esteja pronta
            Platform.runLater(this::atualizarPilhas);
            
            System.out.println("MainWindowController inicializado com sucesso.");
            
        } catch (Exception e) {
            System.err.println("ERRO durante a inicialização do MainWindowController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retorna o controlador das pilhas de tarefas
     */
    public PilhasDeTarefasController getPilhasController() {
        return pilhasDeTarefasController;
    }

    /**
     * Retorna o VBox que contém as pilhas de tarefas (view principal das pilhas).
     */
    public VBox getPilhasDeTarefasView() {
        return pilhasDeTarefasView;
    }

    @FXML
    private void handleSobre() {
        // Abre o diálogo "Sobre" recém-migrado
        
    }
    
    @FXML
    private void handleExit() {
        Platform.exit();
    }
    
    @FXML
    private void handleRefresh() {
        // // Atualiza a visualização da árvore de tarefas
        // if (taskTreeViewController != null) {
        //     taskTreeViewController.refresh();
        //     setStatus("Árvore de tarefas atualizada.");
        // }
        
        // Atualiza as pilhas de tarefas se necessário
        if (pilhasDeTarefasController != null) {
            atualizarPilhas();
            setStatus("Pilhas de tarefas atualizadas.");
        }
    }
    
    @FXML
    private void handleAbrirTesteTransacional() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/TesteTransacional.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Teste Transacional");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Erro ao abrir tela de teste: " + e.getMessage());
        }
    }
    
    /**
     * Atualiza as pilhas de tarefas
     */
    public void atualizarPilhas() {
        if (pilhasDeTarefasController != null) {
            ApplicationContext context = JavaFxSpringApplication.getSpringContext();
           TarefaService tarefaService = context.getBean(TarefaService.class);
if (tarefaService != null) {
                List<TarefaComposta> tarefas = tarefaService.listarTarefasCompostasPorPrioridade();
                if (tarefas != null) {
                    List<Stack<TarefaComposta>> pilhas = new ArrayList<>();
                    Stack<TarefaComposta> pilhaAtual = new Stack<>();
                    Integer ultimaPrioridade = null;
                                pilhas.add(pilhaAtual);

                    for (TarefaComposta t : tarefas) {
                        Integer prioridadeAtual = t.getPrioridade();
                        if (prioridadeAtual == null) {
                            prioridadeAtual = 99;
                        }

                        if (ultimaPrioridade == null || !ultimaPrioridade.equals(prioridadeAtual)) {
                            pilhaAtual = new Stack<>();
                            pilhas.add(pilhaAtual);
                            ultimaPrioridade = prioridadeAtual;
                        }

                        pilhaAtual.push(t);
                    }

                    
                    Platform.runLater(() -> {
                        pilhasDeTarefasController.atualizarPilhas(pilhas);
                        setStatus("Pilhas de tarefas atualizadas. Total de pilhas: " + pilhas.size());
                    });
                } else {
                    Platform.runLater(() -> setStatus("Nenhuma tarefa encontrada"));
                }
            } else {
                Platform.runLater(() -> setStatus("Erro: DAOTarefaLocal não disponível"));
            }
        } else {
            System.err.println("pilhasDeTarefasController não está disponível");
            Platform.runLater(() -> setStatus("Erro: Controlador de pilhas não disponível"));
        }
    }

    /**
     * Define o texto da barra de status
     */
    public void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
}