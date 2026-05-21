package org.hexanet.eventhub.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenManager {
    private static final String TELA_LOGIN = "/org/hexanet/eventhub/auth/Login.fxml";
    private static final String TELA_CADASTRO = "/org/hexanet/eventhub/auth/auth/Cadastro.fxml";

    private static void carregarCena(Stage stage, String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.exibirAlerta(Alert.AlertType.ERROR, "Erro ao carregar cena " + titulo, e.getMessage() );
        }
    }

    public static void abrirLogin(Stage stage) {
        carregarCena(stage, TELA_LOGIN, "EventHub - Login");
    }

    public static void abrirCadastro(Stage stage) {
        carregarCena(stage, TELA_CADASTRO, "EventHub - Criar Conta");
    }
}
