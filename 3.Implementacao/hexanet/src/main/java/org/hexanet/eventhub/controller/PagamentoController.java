package org.hexanet.eventhub.controller;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hexanet.eventhub.dto.ComprarIngressoDTO;
import org.hexanet.eventhub.exceptions.IngressoNaoDisponivelException;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.enums.MetodoPagamento;
import org.hexanet.eventhub.service.ComprarIngressoService;
import org.hexanet.eventhub.utils.AlertManager;
import org.hexanet.eventhub.dto.TipoIngressoDTO;

import java.util.List;

public class PagamentoController {

    private ComprarIngressoDTO comprarIngressoDTO;

    private ComprarIngressoService comprarIngressoService = new ComprarIngressoService();

    @FXML private ToggleGroup grupoPagamento;
    @FXML private RadioButton rbPix;
    @FXML private RadioButton rbCredito;
    @FXML private RadioButton rbDebito;
    @FXML private RadioButton rbBoleto;

    @FXML private TableView<TipoIngressoDTO> tvResumoItens;

    @FXML private Label lblValorTotal;
    @FXML private TableColumn<TipoIngressoDTO, String> colIngressoNome;
    @FXML private TableColumn<TipoIngressoDTO, Double> colIngressoPreco;
    @FXML private TableColumn<TipoIngressoDTO, Integer> colIngressoQtd;


    public void initData(ComprarIngressoDTO dto) {
        this.comprarIngressoDTO = dto;
        carragarDados();
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

    private void carragarDados() {
        colIngressoNome.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getNome())
        );
        colIngressoPreco.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getPreco()).asObject()
        );
        colIngressoQtd.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getQtdDisponiveis()).asObject()
        );

        List<Ingresso> ingressosModel = this.comprarIngressoDTO.getIngressosSelecionados();
        List<TipoIngressoDTO> listaDTOs = ingressosModel.stream()
                .map(ingresso -> new TipoIngressoDTO(
                        ingresso.getNome(),
                        ingresso.getPreco(),
                        ingresso.getQuantidadeSelecionada() // ou o contador do Spinner
                ))
                .toList();
        ObservableList<TipoIngressoDTO> dadosObservaveis = FXCollections.observableArrayList(
                this.comprarIngressoDTO.getIngressosSelecionados()
        );
        tvResumoItens.setItems(dadosObservaveis);
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
