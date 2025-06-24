package com.antoniodiego.javafxspring.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Dialogo "Sobre" migrado do Swing para JavaFX.
 * Responsável por exibir informações da aplicação.
 */
public class TelaSobreController implements Initializable {

    @FXML
    private TextArea areaSobre;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StringBuilder texto = new StringBuilder("Gerenciador de Tarefas\n");
        texto.append("Versão: ").append(Constantes.VERS).append("\n");
        texto.append("Feito por: Antônio Diego\n");
        texto.append("E-mail: antoniodiegoluz@gmail.com\n");
      /*   texto.append("Usuário padrão: ").append(Constantes.NOME_USR_PADR)
             .append("  Senha: ").append(Constantes.SENHA_PADR).append("\n");
 */
        areaSobre.setText(texto.toString());
    }

    /**
     * Utilitário estático para abrir o diálogo de forma modal.
     */
    public static void show(Window owner) {
        try {
            FXMLLoader loader = new FXMLLoader(TelaSobreController.class.getResource("/br/antoniodiego/gertarefas/javafx/ui/sobre/TelaSobre.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Sobre");
            stage.setScene(new Scene(root));
            if (owner != null) {
                stage.initOwner(owner);
            }
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 