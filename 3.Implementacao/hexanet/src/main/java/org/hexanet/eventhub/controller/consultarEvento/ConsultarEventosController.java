package org.hexanet.eventhub.controller.consultarEvento;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.hexanet.eventhub.dto.DetalhesEventoDTO;
import org.hexanet.eventhub.service.ConsultarEventosService;
import org.hexanet.eventhub.utils.AlertManager;
import org.hexanet.eventhub.factory.UIFactory;
import java.util.List;

public class ConsultarEventosController {
    private final ConsultarEventosService consultarEventosService = new ConsultarEventosService();

    @FXML
    private VBox containerEventos;

    @FXML
    public void initialize() {
        try {
            List<DetalhesEventoDTO> detalhesEventos = this.consultarEventosService.listarDetalhesPublicos();
            for (DetalhesEventoDTO evento : detalhesEventos) {
                HBox card = UIFactory.criarCardEvento(evento);
                containerEventos.getChildren().add(card); // Adiciona na tela!
            }
        } catch (Exception e) {
            AlertManager.exibirAlerta(Alert.AlertType.ERROR, "Erro ao listar Eventos", e.getMessage());
        }
    }

}
