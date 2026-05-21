package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import org.hexanet.eventhub.model.Pedido;
import org.hexanet.eventhub.service.ComprarIngressoService;

public class ComprarIngressoController {

    private final ComprarIngressoService comprarIngressoService = new ComprarIngressoService();

    private Pedido pedido;

    @FXML
    public void comprarIngressos() {
        this.comprarIngressoService.comprarIngresso(this.pedido);
    }
}
