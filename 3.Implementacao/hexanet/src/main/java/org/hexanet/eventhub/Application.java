package org.hexanet.eventhub;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hexanet.eventhub.singleton.ScreenManager;
import javafx.scene.image.Image;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 1. Vincula a janela do JavaFX ao gerenciador de janelas do Linux (WM_CLASS)
        System.setProperty("javafx.com.id", "org.hexanet.eventhub");
        System.setProperty("glass.win.class", "org.hexanet.eventhub");

        stage.setTitle("EventHub");

        try {
            java.net.URL iconUrl = getClass().getResource("/org/hexanet/eventhub/assets/logoEventHub2.png");
            if (iconUrl == null) {
                iconUrl = getClass().getResource("/assets/logoEventHub2.png");
            }

            if (iconUrl != null) {
                // 2. Configura o ícone para a janela do JavaFX
                stage.getIcons().add(new Image(iconUrl.toExternalForm()));

                // 3. Força o ícone nativo no sistema operacional (AWT Taskbar Fallback)
                if (java.awt.Taskbar.isTaskbarSupported()) {
                    java.awt.Taskbar taskbar = java.awt.Taskbar.getTaskbar();
                    if (taskbar.isSupported(java.awt.Taskbar.Feature.ICON_IMAGE)) {
                        java.awt.Image awtImage = javax.imageio.ImageIO.read(iconUrl);
                        taskbar.setIconImage(awtImage);
                    }
                }
            } else {
                System.err.println("Ícone logoEventHub.png não encontrado no classpath.");
            }
        } catch (Exception e) {
            System.err.println("Não foi possível carregar o ícone da aplicação: " + e.getMessage());
        }

        // 4. Inicializa as telas através do seu ScreenManager
        ScreenManager.getInstancia().setStagePrincipal(stage);
        ScreenManager.getInstancia().carregarLayoutPrincipal();


    }
}
