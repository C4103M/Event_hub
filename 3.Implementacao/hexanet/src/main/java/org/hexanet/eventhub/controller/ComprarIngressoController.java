package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.hexanet.eventhub.dto.ComprarIngressoDTO;
import org.hexanet.eventhub.dto.DetalhesEventoDTO;
import org.hexanet.eventhub.dto.ItemPedidoDTO;
import org.hexanet.eventhub.dto.TipoIngressoDTO;
import org.hexanet.eventhub.model.Evento;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.TipoIngresso;
import org.hexanet.eventhub.service.ComprarIngressoService;
import org.hexanet.eventhub.service.TipoIngressoService;
import org.hexanet.eventhub.utils.AlertManager;
import org.hexanet.eventhub.singleton.ScreenManager;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComprarIngressoController {

    @FXML private VBox containerTipoIngresso;
    @FXML private Label lblNomeEvento;
    @FXML private Label lblData;
    @FXML private Label lblHora;
    @FXML private Label lblLocal;
    @FXML private Label lblDescricao;



    @FXML private ImageView bigBanner;
    @FXML private ImageView lowBanner;


    private final Map<TipoIngressoDTO, Spinner<Integer>> mapaContadores = new HashMap<>();
    private Evento eventoBase = new Evento();


    @FXML
    public void initData(DetalhesEventoDTO detalhes) {
//        System.out.printf("\nId recebido %d\n", detalhes.getIdEvento());
//        System.out.printf("\nQantidade recebida %d\n", detalhes.getTiposDisponiveis().get(0).getQtdDisponiveis());
//        System.out.println("Nome recebido " + detalhes.getNome());
//        System.out.println("Local recebido " + detalhes.getLocal());

        carregarDados(detalhes);

        this.eventoBase = detalhes.getEvento();

        carregarImagens(detalhes.getUrlImg());
        carregarOpcoesDeIngresso(detalhes.getTiposDisponiveis());
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

            // System.out.printf("Quantidade disponíveis = %d", ingresso.getQtdDisponiveis());

            Spinner<Integer> spinnerQuantidade = new Spinner<>(0, ingresso.getQtdDisponiveis(), 0); // min: 0, max: 10, inicial: 0
            spinnerQuantidade.setPrefWidth(100.0);
            spinnerQuantidade.setEditable(false);
            spinnerQuantidade.getStyleClass().add("spinner");
            spinnerQuantidade.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
            hBoxAlinhador.getChildren().addAll(vboxTextos, spinnerQuantidade);
            cardItem.getChildren().add(hBoxAlinhador);
            this.containerTipoIngresso.getChildren().add(cardItem);

            // Salvar a referência no mapa para ler no momento da compra
            mapaContadores.put(ingresso, spinnerQuantidade);
        }

    }

    private void carregarDados(DetalhesEventoDTO detalhes) {
        lblNomeEvento.setText(detalhes.getNome());
        lblLocal.setText(detalhes.getLocal());

        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = detalhes.getDataHora().format(formatadorData);
        lblData.setText(dataFormatada);

        DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaFormatada = detalhes.getDataHora().format(formatadorHora);
        lblHora.setText(horaFormatada);

        if(detalhes.getEvento() != null){
            lblDescricao.setText(detalhes.getEvento().getDescricao());
        }

    }

        public void carregarImagens(String urlImg) {
        Image img = null;
        
        if (urlImg != null && !urlImg.trim().isEmpty()) {
            try {
                if (urlImg.startsWith("http://") || urlImg.startsWith("https://") || 
                    urlImg.startsWith("file:") || urlImg.startsWith("jar:")) {
                    img = new Image(urlImg);
                } else {
                    String cleanPath = urlImg;
                    if (cleanPath.startsWith("assets/")) {
                        cleanPath = cleanPath.substring("assets/".length());
                    }
                    
                    java.net.URL resourceUrl = getClass().getResource("/assets/" + cleanPath);
                    if (resourceUrl == null) {
                        resourceUrl = getClass().getResource("/org/hexanet/eventhub/assets/" + cleanPath);
                    }
                    if (resourceUrl == null) {
                        resourceUrl = getClass().getResource(urlImg.startsWith("/") ? urlImg : "/" + urlImg);
                    }
                    
                    if (resourceUrl != null) {
                        img = new Image(resourceUrl.toExternalForm());
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro ao tentar carregar a imagem do evento: " + e.getMessage());
            }
        }
        
        if (img == null) {
            img = new Image("https://picsum.photos/250/150");
        }
        
        bigBanner.setImage(img);
        lowBanner.setImage(img);
    }


    public void irParaPagamento() {
        List<Ingresso> ingressosSelecionados = new ArrayList<>();
        List<ItemPedidoDTO> itensResumo = new ArrayList<>();
        double total = 0;

        for (Map.Entry<TipoIngressoDTO, Spinner<Integer>> entry : mapaContadores.entrySet()) {
            TipoIngressoDTO tipoDTO = entry.getKey();
            int quantidade = entry.getValue().getValue();

            if (quantidade > 0) {
                // Converte TipoIngressoDTO para a entidade de modelo TipoIngresso
                TipoIngresso tipoIngresso = new TipoIngresso();
                tipoIngresso.setId(tipoDTO.getId());
                tipoIngresso.setNome(tipoDTO.getNome());
                tipoIngresso.setPreco(tipoDTO.getPreco());

                // Adiciona na lista física individual de ingressos (para o Service persistir)
                for (int i = 0; i < quantidade; i++) {
                    Ingresso ingresso = new Ingresso();
                    ingresso.setTipo(tipoIngresso);
                    ingresso.setEvento(this.eventoBase); // Mantém o vínculo crucial para o estoque
                    ingressosSelecionados.add(ingresso);

                    total += tipoDTO.getPreco();
                }

                // Adiciona no resumo agrupado (para a Tabela do JavaFX ler)
                itensResumo.add(new ItemPedidoDTO(tipoDTO.getNome(), tipoDTO.getPreco(), quantidade));
            }
        }

        if (ingressosSelecionados.isEmpty()) {
            AlertManager.exibirAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione pelo menos um ingresso.");
            return;
        }

        ComprarIngressoDTO comprarIngressoDTO = new ComprarIngressoDTO();
        comprarIngressoDTO.setIdEvento(this.eventoBase.getId());
        comprarIngressoDTO.setNomeEvento(this.eventoBase.getNome());
        comprarIngressoDTO.setEvento(this.eventoBase);
        comprarIngressoDTO.setIngressosSelecionados(ingressosSelecionados);
        comprarIngressoDTO.setItensResumo(itensResumo);
        comprarIngressoDTO.setValorTotalPedido(total);

        // Navega direto passando o DTO unificado
        ScreenManager.getInstancia().irParaPagamento(comprarIngressoDTO);

    }
}
