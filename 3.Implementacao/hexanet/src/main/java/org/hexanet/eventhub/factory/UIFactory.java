package org.hexanet.eventhub.factory;

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
import org.hexanet.eventhub.dto.HistoricoPedidoDTO;
import org.hexanet.eventhub.dto.ItemPedidoDTO;
import org.hexanet.eventhub.exceptions.PermissaoNegadaException;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.singleton.ScreenManager;
import org.hexanet.eventhub.utils.AlertManager;
import org.hexanet.eventhub.utils.QRCodeUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;

public class UIFactory {

    // 1. Fábrica de Card de Evento
    public static HBox criarCardEvento(@NotNull DetalhesEventoDTO evento) {
        HBox card = new HBox(20.0);
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-radius: 12; -fx-padding: 15; -fx-cursor: hand;");
        card.setMaxWidth(900.0);
        card.setPrefWidth(900.0);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(150.0);
        imageView.setFitWidth(250.0);
        imageView.setPreserveRatio(false);
        imageView.setStyle("-fx-background-radius: 8;");

        String nomeArquivo = evento.getUrlImg();
        configurarImagem(imageView, nomeArquivo); // Metodo auxiliar extraído para não repetir código

        VBox detalhesVBox = new VBox(10.0);
        detalhesVBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(detalhesVBox, Priority.ALWAYS);

        Label lblTitulo = new Label(evento.getNome());
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1E3A8A;");

        String dataHoraFormatada = evento.getDataHora() != null ?
                evento.getDataHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")) : "";

        Label lblInfo = new Label(String.format("📍 %s | 📅 %s", evento.getLocal(), dataHoraFormatada));
        lblInfo.setStyle("-fx-text-fill: #4B5563;");

        String desc = (evento.getEvento() != null && evento.getEvento().getDescricao() != null && !evento.getEvento().getDescricao().trim().isEmpty()
                ? evento.getEvento().getDescricao() : "Sem descrição disponível para esse evento");

        Label lblDescricao = new Label(desc);
        lblDescricao.setStyle("-fx-text-fill: #6B7280;");
        lblDescricao.setWrapText(true);

        Region spacerVertical = new Region();
        VBox.setVgrow(spacerVertical, Priority.ALWAYS);

        HBox linhaAcao = new HBox(15.0);
        linhaAcao.setAlignment(Pos.BOTTOM_LEFT);

        double precoMinimo = (evento.getTiposDisponiveis() != null && !evento.getTiposDisponiveis().isEmpty()) ?
                evento.getTiposDisponiveis().get(0).getPreco() : 0.0;

        Label lblPreco = new Label(String.format("A partir de R$ %.2f", precoMinimo));
        lblPreco.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #10B981;");

        Region spacerHorizontal = new Region();
        HBox.setHgrow(spacerHorizontal, Priority.ALWAYS);

        Button btnComprar = new Button("Comprar Ingresso");
        btnComprar.setOnAction(e -> {
            try {
                ScreenManager.getInstancia().irParaComprarIngressos(evento);
            } catch (PermissaoNegadaException ex) {
                AlertManager.exibirAlerta(Alert.AlertType.INFORMATION, "Permissão Negada", ex.getMessage());
            }
        });

        linhaAcao.getChildren().addAll(lblPreco, spacerHorizontal, btnComprar);
        detalhesVBox.getChildren().addAll(lblTitulo, lblInfo, lblDescricao, spacerVertical, linhaAcao);
        card.getChildren().addAll(imageView, detalhesVBox);

        return card;
    }

    // 2. Fábrica de Card de Pedido
    public static HBox criarCardPedido(@NotNull HistoricoPedidoDTO historicoPedidoDTO) {
        DetalhesEventoDTO detalhesEvento = historicoPedidoDTO.getDetalhesEvento();

        HBox card = new HBox(20.0);
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-radius: 12; -fx-padding: 15; -fx-cursor: hand;");
        card.setMaxWidth(900.0);
        card.setPrefWidth(900.0);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(150.0);
        imageView.setFitWidth(250.0);
        imageView.setPreserveRatio(false);
        imageView.setStyle("-fx-background-radius: 8;");

        configurarImagem(imageView, detalhesEvento.getUrlImg()); // Reutilizando a lógica!

        VBox detalhesVBox = new VBox(8.0);
        detalhesVBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(detalhesVBox, Priority.ALWAYS);

        Label lblPedidoId = new Label("Pedido #" + historicoPedidoDTO.getIdPedido());
        lblPedidoId.setStyle("-fx-font-weight: bold; -fx-text-fill: #6B7280; -fx-font-size: 12px;");

        Label lblTitulo = new Label(detalhesEvento.getNome());
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1E3A8A;");

        String dataHoraFormatada = detalhesEvento.getDataHora() != null ?
                detalhesEvento.getDataHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")) : "";
        Label lblInfo = new Label(String.format("📍 %s | 📅 %s", detalhesEvento.getLocal(), dataHoraFormatada));
        lblInfo.setStyle("-fx-text-fill: #4B5563;");

        VBox boxIngressos = new VBox(3.0);
        Label lblItensTitulo = new Label("Ingressos:");
        lblItensTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #374151;");
        boxIngressos.getChildren().add(lblItensTitulo);

        if (historicoPedidoDTO.getItensComprados() != null) {
            for (ItemPedidoDTO item : historicoPedidoDTO.getItensComprados()) {
                Label lblItem = new Label(String.format("• %dx %s", item.getQuantidade(), item.getNome()));
                lblItem.setStyle("-fx-text-fill: #4B5563;");
                boxIngressos.getChildren().add(lblItem);
            }
        }

        Region spacerVertical = new Region();
        VBox.setVgrow(spacerVertical, Priority.ALWAYS);

        HBox linhaAcao = new HBox(15.0);
        linhaAcao.setAlignment(Pos.BOTTOM_LEFT);

        Label lblTotal = new Label(String.format("Total Pago: R$ %.2f", historicoPedidoDTO.getValorTotalPago()));
        lblTotal.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #10B981;");

        Region spacerHorizontal = new Region();
        HBox.setHgrow(spacerHorizontal, Priority.ALWAYS);

        Button btnIrDetalhesPedido = new Button("Ver detalhes do pedido");
        String status = historicoPedidoDTO.getStatusEvento() != null ? historicoPedidoDTO.getStatusEvento().name() : "";

        if (status.equals("ABERTO") || status.equals("EM_ANDAMENTO")) {
            btnIrDetalhesPedido.setStyle("-fx-background-color: #1E3A8A; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");
            btnIrDetalhesPedido.setOnAction(e -> ScreenManager.getInstancia().irParaDetalhesPedido(historicoPedidoDTO.getListaIngressos()));
        } else {
            btnIrDetalhesPedido.setDisable(true);
            btnIrDetalhesPedido.setText("Evento Encerrado");
            btnIrDetalhesPedido.setStyle("-fx-background-color: #D1D5DB; -fx-text-fill: #6B7280; -fx-background-radius: 6; -fx-padding: 8 15;");
        }

        linhaAcao.getChildren().addAll(lblTotal, spacerHorizontal, btnIrDetalhesPedido);
        detalhesVBox.getChildren().addAll(lblPedidoId, lblTitulo, lblInfo, boxIngressos, spacerVertical, linhaAcao);
        card.getChildren().addAll(imageView, detalhesVBox);

        return card;
    }

    // 3. Fábrica de UM Card de Ingresso (Extraído do laço for)
    public static HBox criarCardIngresso(Ingresso ingresso) {
        HBox ticketCard = new HBox();
        ticketCard.getStyleClass().add("ticket-card");
        ticketCard.setAlignment(Pos.CENTER_LEFT);
        ticketCard.setSpacing(20);

        VBox colunaDados = new VBox();
        colunaDados.setSpacing(8);
        HBox.setHgrow(colunaDados, Priority.ALWAYS);

        String nomeEvento = ingresso.getEvento() != null ? ingresso.getEvento().getNome() : "";
        Label lblNomeEvento = new Label(nomeEvento);
        lblNomeEvento.getStyleClass().add("ticket-event-title");

        String dataHoraFormatada = ingresso.getEvento() != null && ingresso.getEvento().getDataHora() != null ?
                ingresso.getEvento().getDataHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")) : "";
        Label lblDataHora = new Label("📅 " + dataHoraFormatada);
        lblDataHora.getStyleClass().add("label-subtitle");

        String nomeTitular = (ingresso.getPedido() != null && ingresso.getPedido().getParticipante() != null) ?
                ingresso.getPedido().getParticipante().getNome() : "Não informado";
        Label lblTitular = new Label("👤 Titular: " + nomeTitular);
        lblTitular.setStyle("-fx-font-weight: bold;");

        HBox linhaTipo = new HBox();
        linhaTipo.setAlignment(Pos.CENTER_LEFT);
        linhaTipo.setSpacing(10);

        String nomeTipo = (ingresso.getTipo() != null && ingresso.getTipo().getNome() != null) ? ingresso.getTipo().getNome().toUpperCase() : "COMUM";
        double preco = ingresso.getTipo() != null ? ingresso.getTipo().getPreco() : 0.0;

        Label badgeTipo = new Label(nomeTipo);
        badgeTipo.getStyleClass().add("ticket-badge");
        Label lblPreco = new Label(String.format("Preço: R$ %.2f", preco));
        lblPreco.getStyleClass().add("label-subtitle");

        linhaTipo.getChildren().addAll(badgeTipo, lblPreco);
        colunaDados.getChildren().addAll(lblNomeEvento, lblDataHora, lblTitular, linhaTipo);
        colunaDados.getStyleClass().add("ticket-divider");

        VBox colunaQrCode = new VBox();
        colunaQrCode.setAlignment(Pos.CENTER);
        colunaQrCode.setPrefWidth(150);
        colunaQrCode.setSpacing(5);

        String conteudoToken = QRCodeUtil.formatarDadosQrCode(ingresso);
        Image imagemDoQrCode = QRCodeUtil.gerarQRCodeJavaFX(conteudoToken, 120, 120);

        ImageView ivQrCode = new ImageView(imagemDoQrCode);
        ivQrCode.setFitWidth(120);
        ivQrCode.setFitHeight(120);
        ivQrCode.setPreserveRatio(true);

        Label lblIdIngresso = new Label("ID: #" + ingresso.getId());
        lblIdIngresso.setStyle("-fx-font-size: 10px; -fx-text-fill: #9CA3AF;");

        colunaQrCode.getChildren().addAll(ivQrCode, lblIdIngresso);
        ticketCard.getChildren().addAll(colunaDados, colunaQrCode);

        // A Factory RETORNA o card em vez de adicionar na tela.
        return ticketCard;
    }

    private static void configurarImagem(ImageView imageView, String nomeArquivo) {
        if (nomeArquivo != null && !nomeArquivo.trim().isEmpty()) {
            try {
                if (nomeArquivo.startsWith("file:") || nomeArquivo.startsWith("http://") ||
                        nomeArquivo.startsWith("https://") || nomeArquivo.startsWith("jar:")) {
                    imageView.setImage(new Image(nomeArquivo));
                } else {
                    String cleanPath = nomeArquivo.replace("assets/images/", "").replace("assets/", "");

                    URL url = UIFactory.class.getResource("/org/hexanet/eventhub/assets/" + cleanPath);
                    if (url == null) url = UIFactory.class.getResource("/assets/" + cleanPath);
                    if (url == null) url = UIFactory.class.getResource(nomeArquivo.startsWith("/") ? nomeArquivo : "/" + nomeArquivo);

                    if (url != null) {
                        imageView.setImage(new Image(url.toExternalForm()));
                    } else {
                        File externalFile = new File(nomeArquivo);
                        if (externalFile.exists()) imageView.setImage(new Image(externalFile.toURI().toString()));
                        else imageView.setImage(new Image("https://picsum.photos/250/150", true));
                    }
                }
            } catch (Exception e) {
                imageView.setImage(new Image("https://picsum.photos/250/150", true));
            }
        } else {
            imageView.setImage(new Image("https://picsum.photos/250/150", true));
        }
    }
}