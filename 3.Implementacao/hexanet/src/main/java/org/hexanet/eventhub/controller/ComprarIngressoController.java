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
import org.hexanet.eventhub.dto.DetalhesEventoDTO;
import org.hexanet.eventhub.dto.TipoIngressoDTO;
import org.hexanet.eventhub.model.Evento;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.TipoIngresso;
import org.hexanet.eventhub.service.ComprarIngressoService;
import org.hexanet.eventhub.service.TipoIngressoService;
import org.hexanet.eventhub.utils.AlertManager;
import org.hexanet.eventhub.singleton.ScreenManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComprarIngressoController {

    @FXML private VBox containerTipoIngresso;
    @FXML private Label lblNomeEvento;
    @FXML private Label lblDataLocal;

    private final Map<TipoIngressoDTO, Spinner<Integer>> mapaContadores = new HashMap<>();
    private Evento eventoBase = new Evento();

    private final ComprarIngressoService comprarIngressoService = new ComprarIngressoService();
    private final TipoIngressoService tipoIngressoService = new TipoIngressoService();



    @FXML
    public void initData(DetalhesEventoDTO detalhes) {
        this.lblNomeEvento.setText(detalhes.getNome());
        this.lblDataLocal.setText(detalhes.getLocal() + " - " + detalhes.getDataHora().toString());

        this.eventoBase.setId(detalhes.getIdEvento());
        this.eventoBase.setNome(detalhes.getNome());


        carregarOpcoesDeIngresso(detalhes.getTiposDisponiveis());

//        eventoBase.setId(1L);
//        List<TipoIngressoDTO> listaIngressosDoBanco = tipoIngressoService.buscarTiposIngressoPorEvento(evento.getId());
//        carregarOpcoesDeIngresso(listaIngressosDoBanco);
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

    public void irParaPagamento() {
        List<Ingresso> ingressosSelecionados = new ArrayList<>();
        double valorTotal = 0.0;

        for(Map.Entry<TipoIngressoDTO, Spinner<Integer>> entry : mapaContadores.entrySet()) {
            TipoIngressoDTO tipoDTO = entry.getKey();
            Spinner<Integer> spinner = entry.getValue();
            int quantidade = spinner.getValue();

            if(quantidade > 0) {
                TipoIngresso tipoEntity = new TipoIngresso();
                tipoEntity.setId(tipoDTO.getId());
                tipoEntity.setNome(tipoDTO.getNome());
                tipoEntity.setPreco(tipoDTO.getPreco());

                for (int i = 0; i < quantidade; i++) {
                    Ingresso ingresso = new Ingresso();
                    ingresso.setTipo(tipoEntity);
                    ingresso.setEvento(this.eventoBase); // CRUCIAL para o service subtrair a QTD!

                    ingressosSelecionados.add(ingresso);
                    valorTotal += tipoDTO.getPreco(); // Soma no total
                }
            }
            if (ingressosSelecionados.isEmpty()) {
                AlertManager.exibirAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione pelo menos um ingresso.");
                return;
            }
            ComprarIngressoDTO carrinhoDTO = new ComprarIngressoDTO();
            carrinhoDTO.setIdEvento(this.eventoBase.getId());
            carrinhoDTO.setNomeEvento(this.eventoBase.getNome());
            carrinhoDTO.setIngressosSelecionados(ingressosSelecionados);
            carrinhoDTO.setValorTotalPedido(valorTotal);

            ScreenManager.getInstancia().irParaTelaPagamento(carrinhoDTO);
        }

    }

//    @FXML
//    public void initialize() {
//        // MODO DESENVOLVIMENTO: Simular a chegada de dados do Ecrã A
//        // ATENÇÃO: Apagar ou comentar esta linha quando a integração real for feita!
//        simularChegadaDoEcraA();
//    }
//    /**
//     * FUNÇÃO TEMPORÁRIA DE MOCK
//     * Cria dados estáticos na memória para poderes testar a renderização
//     * e a passagem para o Ecrã C sem precisares da base de dados ou do Ecrã A.
//     */
//    private void simularChegadaDoEcraA() {
//        DetalhesEventoDTO mockDTO = new DetalhesEventoDTO();
//        mockDTO.setIdEvento(99L);
//        mockDTO.setNome("Festa Junina Teste (MOCK)");
//        mockDTO.setLocal("Pavilhão Central");
//        mockDTO.setDataHora(LocalDateTime.now().plusDays(10)); // Evento daqui a 10 dias
//
//        // Criar opções de ingressos falsas
//        List<TipoIngressoDTO> tiposMocks = new ArrayList<>();
//
//        TipoIngressoDTO tipo1 = new TipoIngressoDTO();
//        tipo1.setId(1L);
//        tipo1.setNome("VIP - 1º Lote");
//        tipo1.setPreco(150.50);
//        tipo1.setQtdDisponiveis(10); // Máximo que o spinner vai aceitar
//
//        TipoIngressoDTO tipo2 = new TipoIngressoDTO();
//        tipo2.setId(2L);
//        tipo2.setNome("Pista - 2º Lote");
//        tipo2.setPreco(50.00);
//        tipo2.setQtdDisponiveis(5);
//
//        tiposMocks.add(tipo1);
//        tiposMocks.add(tipo2);
//
//        mockDTO.setTiposDisponiveis(tiposMocks);
//
//        // Força a injeção como se o ScreenManager tivesse acabado de chamar
//        initData(mockDTO);
//    }
}
