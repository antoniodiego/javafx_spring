package com.antoniodiego.javafxspring;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class JavaFxSpringApplication extends Application {

    static ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        // Inicializa o contexto Spring
        springContext = SpringApplication.run(JavaFxSpringApplication.class);
    }

    public static ApplicationContext getSpringContext() {
        return springContext;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carrega o FXML
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));

        // Define o factory para injeção de dependência
        fxmlLoader.setControllerFactory(springContext::getBean);

        Parent root = fxmlLoader.load();
        // Configura a janela principal
        primaryStage.setTitle("JavaFX + Spring + Hibernate - Gerenciamento de Usuários");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        // Centraliza a janela
        primaryStage.centerOnScreen();

        primaryStage.show();

        // Configura o fechamento da aplicação
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @Override
    public void stop() {
        // Fecha o contexto Spring ao sair
        if (springContext != null) {
            springContext.close();
        }
    }

    public static void main(String[] args) {
        // Define propriedades do sistema para JavaFX
        System.setProperty("java.awt.headless", "false");

        // Inicia a aplicação JavaFX
        launch(args);
    }
}
