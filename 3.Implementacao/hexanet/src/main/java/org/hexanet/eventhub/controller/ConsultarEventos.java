package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.hexanet.eventhub.dto.DetalhesEventoDTO;
import org.hexanet.eventhub.service.ManterEventoService;
import org.hexanet.eventhub.singleton.ScreenManager;
import org.hexanet.eventhub.utils.AlertManager;

import java.net.URL;
import java.util.List;

public class ConsultarEventos {
    private ManterEventoService manterEventoService = new ManterEventoService();

    @FXML
    private VBox containerEventos;

    @FXML
    public void initialize() {
        try {
            List<DetalhesEventoDTO> detalhesEventos = this.manterEventoService.listarDetalhes();
            for (DetalhesEventoDTO evento : detalhesEventos) {
                HBox card = criarCardEvento(evento);
                containerEventos.getChildren().add(card); // Adiciona na tela!
            }
        } catch (Exception e) {
            AlertManager.exibirAlerta(Alert.AlertType.ERROR, "Erro ao listar Eventos", e.getMessage());
        }
    }

    private HBox criarCardEvento(DetalhesEventoDTO evento) {
        // Container Principal do Card
        HBox card = new HBox(20.0);
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-radius: 12; -fx-padding: 15; -fx-cursor: hand;");
        card.setMaxWidth(900.0);
        card.setPrefWidth(900.0);

        // Imagem
        ImageView imageView = new ImageView();
        imageView.setFitHeight(150.0);
        imageView.setFitWidth(250.0);
        imageView.setPreserveRatio(false);
        imageView.setStyle("-fx-background-radius: 8;");

        // --- INÍCIO DA LÓGICA DE IMAGEM ADAPTADA ---
        String nomeArquivo = evento.getUrlImg(); // Pegando a string da imagem do DTO

        if (nomeArquivo != null && !nomeArquivo.trim().isEmpty()) {
            String cleanPath = nomeArquivo;

            // Limpando o caminho assim como no configurarColunaBanner
            if (cleanPath.startsWith("assets/images/")) {
                cleanPath = cleanPath.substring("assets/images/".length());
            } else if (cleanPath.startsWith("assets/")) {
                cleanPath = cleanPath.substring("assets/".length());
            }

            // Tentando resolver a URL em diferentes locais possíveis
            URL url = getClass().getResource("/org/hexanet/eventhub/assets/" + cleanPath);
            if (url == null) {
                url = getClass().getResource("/assets/" + cleanPath);
            }
            if (url == null) {
                url = getClass().getResource(nomeArquivo.startsWith("/") ? nomeArquivo : "/" + nomeArquivo);
            }

            if (url != null) {
                imageView.setImage(new Image(url.toExternalForm()));
            } else {
                System.out.println("Imagem não encontrada no classpath para: " + evento.getNome() + " (Caminho tentado: " + cleanPath + ")");
            }
        } else {
            imageView.setImage(new Image("https://picsum.photos/250/150", true));
        }
        // --- FIM DA LÓGICA DE IMAGEM ---

        // Detalhes em VBox
        VBox detalhesVBox = new VBox(10.0);
        detalhesVBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(detalhesVBox, Priority.ALWAYS);

        Label lblTitulo = new Label(evento.getNome());
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1E3A8A;");

        // Formatando local e data dinamicamente (ajuste os getters se necessário)
        Label lblInfo = new Label(String.format("📍 %s | \uD83D\uDCC5 %s", evento.getLocal(), evento.getDataHora()));
        lblInfo.setStyle("-fx-text-fill: #4B5563;");

        // Se o seu DTO não possuir getDescricao(), remova ou crie o campo
        Label lblDescricao = new Label("Descrição do evento não disponível.");
        lblDescricao.setStyle("-fx-text-fill: #6B7280;");
        lblDescricao.setWrapText(true);

        // Spacer para empurrar o botão para o fundo
        Region spacerVertical = new Region();
        VBox.setVgrow(spacerVertical, Priority.ALWAYS);

        // Linha Inferior (Preço e Botão)
        HBox linhaAcao = new HBox(15.0);
        linhaAcao.setAlignment(Pos.BOTTOM_LEFT);

        // Aqui precisamos pegar o menor preço da lista de ingressos do DTO
        double precoMinimo = 0.0;
        if (evento.getTiposDisponiveis() != null && !evento.getTiposDisponiveis().isEmpty()) {
            precoMinimo = evento.getTiposDisponiveis().get(0).getPreco(); // Exemplo pegando o primeiro ou implemente um laço para achar o menor
        }

        Label lblPreco = new Label(String.format("A partir de R$ %.2f", precoMinimo));
        lblPreco.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #10B981;");

        Region spacerHorizontal = new Region();
        HBox.setHgrow(spacerHorizontal, Priority.ALWAYS);

        Button btnComprar = new Button("Comprar Ingresso");
        // Adicionando a ação de clique do botão dinâmico!
        btnComprar.setOnAction(e -> {
            try {
                // Chama o seu ScreenManager passando o DTO completo do evento clicado
                ScreenManager.getInstancia().irParaComprarIngressos(evento);

                // Nota: Se o seu singleton estiver em português, talvez seja getInstancia()

            } catch (Exception ex) {
                System.err.println("Erro ao tentar abrir a tela de compra: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        linhaAcao.getChildren().addAll(lblPreco, spacerHorizontal, btnComprar);

        // Montando a Árvore do Layout
        detalhesVBox.getChildren().addAll(lblTitulo, lblInfo, lblDescricao, spacerVertical, linhaAcao);
        card.getChildren().addAll(imageView, detalhesVBox);

        return card;
    }
}
