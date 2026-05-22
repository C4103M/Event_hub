package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.hexanet.eventhub.dto.ComprarIngressoDTO;
import org.hexanet.eventhub.exceptions.IngressoNaoDisponivelException;
import org.hexanet.eventhub.exceptions.PermissaoNegada;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.Pedido;
import org.hexanet.eventhub.model.enums.MetodoPagamento;
import org.hexanet.eventhub.service.ComprarIngressoService;
import org.hexanet.eventhub.utils.AlertManager;

import java.util.List;
public class ComprarIngressoController {

    private final ComprarIngressoService comprarIngressoService = new ComprarIngressoService();

    // Tem que receber do front
    List<Ingresso> ingressos;
    MetodoPagamento metodoPagamento;
    private Pedido pedido;

    @FXML
    public void comprarIngressos() {
        ComprarIngressoDTO comprarIngressoDTO = new ComprarIngressoDTO(this.ingressos, this.metodoPagamento);
        try {
            this.comprarIngressoService.comprarIngresso(comprarIngressoDTO);
        } catch (PermissaoNegada e) {
            AlertManager.exibirAlerta(Alert.AlertType.WARNING, "Permissão Negada", e.getMessage());
        } catch (IngressoNaoDisponivelException e) {
            AlertManager.exibirAlerta(Alert.AlertType.WARNING, "Ingresso indisponível", e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}
