package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.hexanet.eventhub.dto.ComprarIngressoDTO;
import org.hexanet.eventhub.dto.TipoIngressoDTO;
import org.hexanet.eventhub.exceptions.IngressoNaoDisponivelException;
import org.hexanet.eventhub.exceptions.PermissaoNegada;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.Pedido;
import org.hexanet.eventhub.model.enums.MetodoPagamento;
import org.hexanet.eventhub.service.ComprarIngressoService;
import org.hexanet.eventhub.service.TipoIngressoService;
import org.hexanet.eventhub.utils.AlertManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComprarIngressoController {

    private final ComprarIngressoService comprarIngressoService = new ComprarIngressoService();
    private final TipoIngressoService tipoIngressoService = new TipoIngressoService();

    // Tem que receber do front
    MetodoPagamento metodoPagamento;
    private Pedido pedido;

    @FXML
    private VBox containerTipoIngresso;

    private final Map<TipoIngressoDTO, Spinner<Integer>> mapaContadores = new HashMap<>();
    List<Ingresso> ingressos;

    @FXML
    public void initialize() {
        // Exemplo de dados vindo do Service. Substitua pela chamada real do banco:
        // List<TipoIngressoDTO> ingressos = tipoIngressoService.buscarPorEvento(eventoId);
        List<TipoIngressoDTO> listaIngressosDoBanco = tipoIngressoService.buscarTiposIngressoPorEvento(1L);

        carregarOpcoesDeIngresso(listaIngressosDoBanco);
    }

    private void carregarOpcoesDeIngresso(List<TipoIngressoDTO> listaIngressos) {
        // Limpa o contêiner
        this.containerTipoIngresso.getChildren().clear();
        mapaContadores.clear();
        for (TipoIngressoDTO ingresso : listaIngressos) {
            VBox cardItem = new VBox(5);
            cardItem.setStyle("-fx-background-color: #F9FAFB; -fx-padding: 15; -fx-background-radius: 8; -fx-border-color: #E5E7EB; -fx-border-radius: 8;");
            HBox hBoxAlinhador = new HBox();
            hBoxAlinhador.setAlignment(Pos.CENTER_LEFT);
            VBox vboxTextos = new VBox();
            HBox.setHgrow(vboxTextos, Priority.ALWAYS);
            Label lblNome = new Label(ingresso.getNome());
            lblNome.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");
            // Formata o preço em moeda local
            Label lblPreco = new Label(String.format("R$ %.2f + taxa", ingresso.getPreco()));
            lblPreco.getStyleClass().add("label-subtitle");
            vboxTextos.getChildren().addAll(lblNome, lblPreco);
            Spinner<Integer> spinnerQuantidade = new Spinner<>(0, ingresso.getQtdDisponiveis(), 0); // min: 0, max: 10, inicial: 0
            spinnerQuantidade.setPrefWidth(100.0);
            spinnerQuantidade.setEditable(false);
            spinnerQuantidade.getStyleClass().add("spinner");
            spinnerQuantidade.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
            hBoxAlinhador.getChildren().addAll(vboxTextos, spinnerQuantidade);
            cardItem.getChildren().add(hBoxAlinhador);
            this.containerTipoIngresso.getChildren().add(cardItem);

            // . Salvar a referência no mapa para ler no momento da compra
            mapaContadores.put(ingresso, spinnerQuantidade);
        }

    }


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
