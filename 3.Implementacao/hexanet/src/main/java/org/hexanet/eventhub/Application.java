package org.hexanet.eventhub;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hexanet.eventhub.singleton.ScreenManager;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("EventHub");
        ScreenManager.getInstancia().setStagePrincipal(stage);
        ScreenManager.getInstancia().carregarLayoutPrincipal();
//        ScreenManager.getInstancia().irParaConsultarEventos();
//        stage.show();
    }
}
