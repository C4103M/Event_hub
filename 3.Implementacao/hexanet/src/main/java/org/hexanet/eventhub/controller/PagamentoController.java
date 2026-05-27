package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hexanet.eventhub.dto.ComprarIngressoDTO;
import org.hexanet.eventhub.exceptions.IngressoNaoDisponivelException;
import org.hexanet.eventhub.exceptions.PermissaoNegada;
import org.hexanet.eventhub.model.enums.MetodoPagamento;
import org.hexanet.eventhub.service.ComprarIngressoService;
import org.hexanet.eventhub.utils.AlertManager;

public class PagamentoController {

    private ComprarIngressoDTO comprarIngressoDTO;

    private ComprarIngressoService comprarIngressoService = new ComprarIngressoService();

    @FXML private ToggleGroup grupoPagamento;
    @FXML private RadioButton rbPix;
    @FXML private RadioButton rbCredito;
    @FXML private RadioButton rbDebito;
    @FXML private RadioButton rbBoleto;

    @FXML private TableView tvResumoItens;
    @FXML private Label lbValorTotal;

    public void initData(ComprarIngressoDTO dto) {
        this.comprarIngressoDTO = dto;
    }
    @FXML
    private void confirmarPagamento() {
        RadioButton selecionado = (RadioButton) grupoPagamento.getSelectedToggle();

        if (selecionado != null) {
            String textoSelecionado = (String) selecionado.getUserData();
            System.out.println("Método escolhido: " + textoSelecionado);

            MetodoPagamento metodo = MetodoPagamento.fromString(textoSelecionado);

            comprarIngressos(metodo);

        } else {
            System.out.println("Nenhum método selecionado!");
        }
    }

    public void comprarIngressos(MetodoPagamento metodoPagamento) {
        this.comprarIngressoDTO.setMetodoPagamento(metodoPagamento);
        try {
            this.comprarIngressoService.comprarIngresso(comprarIngressoDTO);
        } catch (IngressoNaoDisponivelException e) {
            AlertManager.exibirAlerta(Alert.AlertType.WARNING, "Ingresso indisponível", e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}
