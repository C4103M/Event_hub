package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.utils.IngressarManager;
import org.hexanet.eventhub.utils.QRCodeUtil;

import java.util.List;

public class DetalhesPedidoController {

    @FXML
    private VBox containerIngressos;

    /**
     * Método chamado pelo ScreenManager para injetar os dados do Pedido nesta tela.
     */
    public void initData(List<Ingresso> ingressosDoPedido) {
        // 1. Limpa o container para não duplicar dados caso a tela seja reaberta
        containerIngressos.getChildren().clear();

        // 2. Loop pelos ingressos que vieram da camada de Service
        for (Ingresso ingresso : ingressosDoPedido) {

            // Construir o Card Principal (HBox horizontal)
            HBox ticketCard = new HBox();
            ticketCard.getStyleClass().add("ticket-card");
            ticketCard.setAlignment(Pos.CENTER_LEFT);
            ticketCard.setSpacing(20);

            // ==========================================
            // COLUNA DA ESQUERDA: Dados do Ingresso
            // ==========================================
            VBox colunaDados = new VBox();
            colunaDados.setSpacing(8);
            HBox.setHgrow(colunaDados, Priority.ALWAYS); // Ocupa todo o espaço restante da esquerda

            String nomeEvento = "";
            java.time.LocalDateTime dataHora = null;
            if (ingresso.getEvento() != null) {
                nomeEvento = ingresso.getEvento().getNome();
                dataHora = ingresso.getEvento().getDataHora();
            }

            Label lblNomeEvento = new Label(nomeEvento);
            lblNomeEvento.getStyleClass().add("ticket-event-title");

            String dataHoraFormatada = "";
            if (dataHora != null) {
                java.time.format.DateTimeFormatter formatador = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
                dataHoraFormatada = dataHora.format(formatador);
            }
            Label lblDataHora = new Label("📅 " + dataHoraFormatada);
            lblDataHora.getStyleClass().add("label-subtitle");

            String nomeTitular = "Não informado";
            if (ingresso.getPedido() != null && ingresso.getPedido().getParticipante() != null) {
                nomeTitular = ingresso.getPedido().getParticipante().getNome();
            }
            Label lblTitular = new Label("👤 Titular: " + nomeTitular);
            lblTitular.setStyle("-fx-font-weight: bold;");

            // Linha horizontal interna para o Tipo de Ingresso
            HBox linhaTipo = new HBox();
            linhaTipo.setAlignment(Pos.CENTER_LEFT);
            linhaTipo.setSpacing(10);

            String nomeTipo = "COMUM";
            double preco = 0.0;
            if (ingresso.getTipo() != null) {
                if (ingresso.getTipo().getNome() != null) {
                    nomeTipo = ingresso.getTipo().getNome().toUpperCase();
                }
                preco = ingresso.getTipo().getPreco();
            }

            Label badgeTipo = new Label(nomeTipo);
            badgeTipo.getStyleClass().add("ticket-badge");

            Label lblPreco = new Label(String.format("Preço: R$ %.2f", preco));
            lblPreco.getStyleClass().add("label-subtitle");

            linhaTipo.getChildren().addAll(badgeTipo, lblPreco);

            // Adiciona os textos à coluna de dados
            colunaDados.getChildren().addAll(lblNomeEvento, lblDataHora, lblTitular, linhaTipo);
            colunaDados.getStyleClass().add("ticket-divider"); // Aplica o efeito tracejado na direita

            // ==========================================
            // COLUNA DA DIREITA: QR Code de Validação
            // ==========================================
            VBox colunaQrCode = new VBox();
            colunaQrCode.setAlignment(Pos.CENTER);
            colunaQrCode.setPrefWidth(150);
            colunaQrCode.setSpacing(5);

            // Invocamos os nossos utilitários criados nas etapas anteriores
            String conteudoToken = IngressarManager.formatarDadosQrCode(ingresso);
            Image imagemDoQrCode = QRCodeUtil.gerarQRCodeJavaFX(conteudoToken, 120, 120);

            ImageView ivQrCode = new ImageView(imagemDoQrCode);
            ivQrCode.setFitWidth(120);
            ivQrCode.setFitHeight(120);
            ivQrCode.setPreserveRatio(true);

            Label lblIdIngresso = new Label("ID: #" + ingresso.getId());
            lblIdIngresso.setStyle("-fx-font-size: 10px; -fx-text-fill: #9CA3AF;");

            colunaQrCode.getChildren().addAll(ivQrCode, lblIdIngresso);

            // ==========================================
            // JUNTAR AS DUAS PARTES NO CARD E ADICIONAR À TELA
            // ==========================================
            ticketCard.getChildren().addAll(colunaDados, colunaQrCode);

            // Injeta o card fisicamente no VBox do FXML
            containerIngressos.getChildren().add(ticketCard);
        }
    }
}