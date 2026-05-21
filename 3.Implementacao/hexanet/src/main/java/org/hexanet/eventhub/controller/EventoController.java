package org.hexanet.eventhub.controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EventoController implements Initializable {

    @FXML
    private ComboBox<String> cbxBannerEvento; // Corrigido aqui!

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarImagensDaPasta();
    }

    private void carregarImagensDaPasta() {
        File pastaAssets = new File("src/main/resources/assets");

        if (pastaAssets.exists() && pastaAssets.isDirectory()) {
            File[] arquivos = pastaAssets.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".png") ||
                            name.toLowerCase().endsWith(".jpg") ||
                            name.toLowerCase().endsWith(".jpeg")
            );

            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    cbxBannerEvento.getItems().add(arquivo.getName()); // Corrigido aqui!
                }
            }
        } else {
            System.out.println("Aviso: Pasta assets não encontrada.");
        }
    }
}
//Telas necessárias: Criar, remover, editar e excluir
//Usuarios permitidos: ADM, organizador cujo evento esteja relacionado