package org.hexanet.eventhub.controller.comprarIngresso;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hexanet.eventhub.dto.ComprarIngressoDTO;
import org.hexanet.eventhub.dto.ItemPedidoDTO;
import org.hexanet.eventhub.exceptions.IngressoNaoDisponivelException;
import org.hexanet.eventhub.model.enums.MetodoPagamento;
import org.hexanet.eventhub.service.ComprarIngressoService;
import org.hexanet.eventhub.singleton.ScreenManager;
import org.hexanet.eventhub.utils.AlertManager;

public class PagamentoController {

    private ComprarIngressoDTO comprarIngressoDTO;

    private final ComprarIngressoService comprarIngressoService = new ComprarIngressoService();

    @FXML private ToggleGroup grupoPagamento;
    @FXML private RadioButton rbPix;
    @FXML private RadioButton rbCredito;
    @FXML private RadioButton rbDebito;
    @FXML private RadioButton rbBoleto;

    @FXML private Label lblValorTotal;
    @FXML private TableView<ItemPedidoDTO> tvResumoItens;
    @FXML private TableColumn<ItemPedidoDTO, String> colIngressoNome;
    @FXML private TableColumn<ItemPedidoDTO, Double> colIngressoPreco;
    @FXML private TableColumn<ItemPedidoDTO, Integer> colIngressoQtd;


    public void initData(ComprarIngressoDTO dto) {
        this.comprarIngressoDTO = dto;
        carregarDados();
    }

    private void carregarDados() {
        if (this.comprarIngressoDTO == null) return;

        colIngressoNome.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNome())
        );
        colIngressoPreco.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getPrecoUnitario()).asObject()
        );
        colIngressoQtd.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getQuantidade()).asObject()
        );

        ObservableList<ItemPedidoDTO> dadosObservaveis = FXCollections.observableArrayList(
                this.comprarIngressoDTO.getItensResumo()
        );
        tvResumoItens.setItems(dadosObservaveis);

        lblValorTotal.setText(String.format("R$ %.2f", this.comprarIngressoDTO.getValorTotalPedido()));
    }

    @FXML
    private void confirmarPagamento() {
        RadioButton selecionado = (RadioButton) grupoPagamento.getSelectedToggle();

        if (selecionado != null) {
            String textoSelecionado = (String) selecionado.getUserData();
//            System.out.println("Método escolhido: " + textoSelecionado);

            MetodoPagamento metodo = MetodoPagamento.fromString(textoSelecionado);

            comprarIngressos(metodo);

        } else {
            AlertManager.exibirAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione uma forma de pagamento.");
        }
    }

    public void comprarIngressos(MetodoPagamento metodoPagamento) {
        this.comprarIngressoDTO.setMetodoPagamento(metodoPagamento);
        try {
            this.comprarIngressoService.comprarIngresso(comprarIngressoDTO);
            AlertManager.exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Compra finalizada com sucesso!");
            ScreenManager.getInstancia().voltarTelaAnterior();
        } catch (IngressoNaoDisponivelException e) {
            AlertManager.exibirAlerta(Alert.AlertType.WARNING, "Ingresso indisponível", e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}
