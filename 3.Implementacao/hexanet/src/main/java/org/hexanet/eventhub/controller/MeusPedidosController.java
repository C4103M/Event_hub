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
import org.hexanet.eventhub.dto.HistoricoPedidoDTO;
import org.hexanet.eventhub.dto.ItemPedidoDTO;
import org.hexanet.eventhub.service.MeusPedidosService;
import org.hexanet.eventhub.singleton.ScreenManager;
import org.hexanet.eventhub.singleton.SessaoUsuario;
import org.hexanet.eventhub.utils.AlertManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.io.File;
import java.net.URL;

public class MeusPedidosController {
    MeusPedidosService meusPedidosService = new MeusPedidosService();

    @FXML private VBox containerPedidos;

    @FXML
    public void initialize() {
        try {
            Long idParticipante = SessaoUsuario.getInstancia().getUsuarioLogado().getId();
//            System.out.println("Id do participante: " + idParticipante);
            List<HistoricoPedidoDTO> listaPedidos = meusPedidosService.listarMeusPedidos(idParticipante);

            for(HistoricoPedidoDTO historicoPedidoDTO : listaPedidos) {
                HBox card = criarCardPedido(historicoPedidoDTO);
                containerPedidos.getChildren().add(card);
            }


        } catch (RuntimeException e) {
            AlertManager.exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private HBox criarCardPedido(@NotNull HistoricoPedidoDTO historicoPedidoDTO) {
        DetalhesEventoDTO detalhesEvento = historicoPedidoDTO.getDetalhesEvento();

        // Container Principal do Card
        HBox card = new HBox(20.0);
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-radius: 12; -fx-padding: 15; -fx-cursor: hand;");
        card.setMaxWidth(900.0);
        card.setPrefWidth(900.0);

        // --- INÍCIO DA LÓGICA DE IMAGEM (Mantida exatamente a sua) ---
        ImageView imageView = new ImageView();
        imageView.setFitHeight(150.0);
        imageView.setFitWidth(250.0);
        imageView.setPreserveRatio(false);
        imageView.setStyle("-fx-background-radius: 8;");

        String nomeArquivo = detalhesEvento.getUrlImg();

        if (nomeArquivo != null && !nomeArquivo.trim().isEmpty()) {
            try {
                if (nomeArquivo.startsWith("file:") || nomeArquivo.startsWith("http://") ||
                        nomeArquivo.startsWith("https://") || nomeArquivo.startsWith("jar:")) {
                    imageView.setImage(new Image(nomeArquivo));
                } else {
                    String cleanPath = nomeArquivo;
                    if (cleanPath.startsWith("assets/images/")) {
                        cleanPath = cleanPath.substring("assets/images/".length());
                    } else if (cleanPath.startsWith("assets/")) {
                        cleanPath = cleanPath.substring("assets/".length());
                    }

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
                        File externalFile = new File(nomeArquivo);
                        if (externalFile.exists()) {
                            imageView.setImage(new Image(externalFile.toURI().toString()));
                        } else {
                            imageView.setImage(new Image("https://picsum.photos/250/150", true));
                        }
                    }
                }
            } catch (Exception e) {
                imageView.setImage(new Image("https://picsum.photos/250/150", true));
            }
        } else {
            imageView.setImage(new Image("https://picsum.photos/250/150", true));
        }
        // --- FIM DA LÓGICA DE IMAGEM ---

        // Detalhes em VBox
        VBox detalhesVBox = new VBox(8.0);
        detalhesVBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(detalhesVBox, Priority.ALWAYS);

        // Identificador do Pedido
        Label lblPedidoId = new Label("Pedido #" + historicoPedidoDTO.getIdPedido());
        lblPedidoId.setStyle("-fx-font-weight: bold; -fx-text-fill: #6B7280; -fx-font-size: 12px;");

        // Título e Informações do Evento
        Label lblTitulo = new Label(detalhesEvento.getNome());
        lblTitulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1E3A8A;");

        // Formatando local e data dinamicamente com DateTimeFormatter
        String dataHoraFormatada = "";
        if (detalhesEvento.getDataHora() != null) {
            java.time.format.DateTimeFormatter formatador = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
            dataHoraFormatada = detalhesEvento.getDataHora().format(formatador);
        }
        Label lblInfo = new Label(String.format("📍 %s | 📅 %s", detalhesEvento.getLocal(), dataHoraFormatada));
        lblInfo.setStyle("-fx-text-fill: #4B5563;");

        // --- LISTA DE INGRESSOS COMPRADOS ---
        VBox boxIngressos = new VBox(3.0);
        Label lblItensTitulo = new Label("Ingressos:");
        lblItensTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #374151;");
        boxIngressos.getChildren().add(lblItensTitulo);

        // Iterando sobre os itens do DTO
        if (historicoPedidoDTO.getItensComprados() != null) {
            for (ItemPedidoDTO item : historicoPedidoDTO.getItensComprados()) {
                Label lblItem = new Label(String.format("• %dx %s", item.getQuantidade(), item.getNome()));
                lblItem.setStyle("-fx-text-fill: #4B5563;");
                boxIngressos.getChildren().add(lblItem);
            }
        }

        // Spacer para empurrar a linha de ação para o fundo
        Region spacerVertical = new Region();
        VBox.setVgrow(spacerVertical, Priority.ALWAYS);

        // --- LINHA INFERIOR (Preço e Botão) ---
        HBox linhaAcao = new HBox(15.0);
        linhaAcao.setAlignment(Pos.BOTTOM_LEFT);

        Label lblTotal = new Label(String.format("Total Pago: R$ %.2f", historicoPedidoDTO.getValorTotalPago()));
        lblTotal.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #10B981;");

        Region spacerHorizontal = new Region();
        HBox.setHgrow(spacerHorizontal, Priority.ALWAYS);

        // Botão de Ingressar
        Button btnIrDetalhesPedido = new Button("Ver detalhes do pedido");

        btnIrDetalhesPedido.setStyle("-fx-background-color: #1E3A8A; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-padding: 8 15; -fx-cursor: hand;");

        // Lógica para exibir o botão APENAS se o evento ainda não tiver encerrado/cancelado
        String status = historicoPedidoDTO.getStatusEvento() != null ? historicoPedidoDTO.getStatusEvento().name() : "";

        if (status.equals("ABERTO") || status.equals("EM_ANDAMENTO")) {
            btnIrDetalhesPedido.setVisible(true);
            btnIrDetalhesPedido.setOnAction(e -> {
                ScreenManager.getInstancia().irParaDetalhesPedido(historicoPedidoDTO.getListaIngressos());
            });
        } else {
            // Se o evento já passou, podemos esconder o botão ou desabilitá-lo para informar o usuário
            btnIrDetalhesPedido.setDisable(true);
            btnIrDetalhesPedido.setText("Evento Encerrado");
            btnIrDetalhesPedido.setStyle("-fx-background-color: #D1D5DB; -fx-text-fill: #6B7280; -fx-background-radius: 6; -fx-padding: 8 15;");
        }

        linhaAcao.getChildren().addAll(lblTotal, spacerHorizontal, btnIrDetalhesPedido);

        // Montando a Árvore do Layout
        detalhesVBox.getChildren().addAll(lblPedidoId, lblTitulo, lblInfo, boxIngressos, spacerVertical, linhaAcao);
        card.getChildren().addAll(imageView, detalhesVBox);

        return card;
    }

}
