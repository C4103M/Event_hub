package org.hexanet.eventhub;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hexanet.eventhub.singleton.ScreenManager;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("MainLayout.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("EventHub");
        stage.setScene(scene);
        ScreenManager.getInstancia().setStagePrincipal(stage);
        stage.show();
    }
}
