package org.hexanet.eventhub.controller.verPedidos;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.hexanet.eventhub.dto.HistoricoPedidoDTO;
import org.hexanet.eventhub.service.MeusPedidosService;
import org.hexanet.eventhub.singleton.SessaoUsuario;
import org.hexanet.eventhub.utils.AlertManager;
import org.hexanet.eventhub.factory.UIFactory;

import java.util.List;


public class MeusPedidosController {
    MeusPedidosService meusPedidosService = new MeusPedidosService();

    @FXML private VBox containerPedidos;

    @FXML
    public void initialize() {
        try {
            Long idParticipante = SessaoUsuario.getInstancia().getUsuarioLogado().getId();
            List<HistoricoPedidoDTO> listaPedidos = meusPedidosService.listarMeusPedidos(idParticipante);

            for(HistoricoPedidoDTO historicoPedidoDTO : listaPedidos) {
                HBox card = UIFactory.criarCardPedido(historicoPedidoDTO);
                containerPedidos.getChildren().add(card);
            }


        } catch (RuntimeException e) {
            AlertManager.exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
