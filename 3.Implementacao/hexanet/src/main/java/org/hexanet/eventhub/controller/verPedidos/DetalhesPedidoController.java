package org.hexanet.eventhub.controller.verPedidos;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.hexanet.eventhub.model.Ingresso;

import java.util.List;
import org.hexanet.eventhub.factory.UIFactory;
public class DetalhesPedidoController {

    @FXML
    private VBox containerIngressos;

    /**
     * Método chamado pelo ScreenManager para injetar os dados do Pedido nesta tela.
     */
    public void initData(List<Ingresso> ingressosDoPedido) {
        containerIngressos.getChildren().clear();
        for (Ingresso ingresso : ingressosDoPedido) {
            HBox ticketCard = UIFactory.criarCardIngresso(ingresso);
            containerIngressos.getChildren().add(ticketCard);
        }
    }
}