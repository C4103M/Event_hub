package org.hexanet.eventhub.singleton;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hexanet.eventhub.controller.AuthController;
import org.hexanet.eventhub.controller.ComprarIngressoController;
import org.hexanet.eventhub.controller.PagamentoController;
import org.hexanet.eventhub.dto.ComprarIngressoDTO;
import org.hexanet.eventhub.dto.DetalhesEventoDTO;

import java.io.IOException;

public class ScreenManager {

    // 1. Instância Singleton
    private static ScreenManager instancia;
    private Stage stagePrincipal;
    private BorderPane painelPrincipal; // Para navegação dinâmica (Estratégia 2)

    private ScreenManager() {}

    public static synchronized ScreenManager getInstancia() {
        if (instancia == null) {
            instancia = new ScreenManager();
        }
        return instancia;
    }

    public void setStagePrincipal(Stage stage) {
        this.stagePrincipal = stage;
    }

    public void setPainelPrincipal(BorderPane borderPane) {
        this.painelPrincipal = borderPane;
    }

    // 2. Caminhos FXML (Privados e Centralizados)



    private static final String TELA_COMPRAR_INGRESSO = "/org/hexanet/eventhub/ingressos/ComprarIngresso.fxml";

    public void irParaTelaComprarIngressos(DetalhesEventoDTO detalhes) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(TELA_COMPRAR_INGRESSO));
            Parent root = loader.load();

            // Injeção de dados via controller
            ComprarIngressoController controller = loader.getController();
            controller.initData(detalhes);

            // Troca o miolo do BorderPane se estiver configurado, ou a cena inteira
            if (painelPrincipal != null) {
                painelPrincipal.setCenter(root);
            } else {
                stagePrincipal.getScene().setRoot(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // AlertManager.exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar o pagamento.");
        }
    }

    private static final String TELA_PAGAMENTO = "/org/hexanet/eventhub/ingressos/Pagamento.fxml";

    // 3. Método Centralizador de Navegação Dinâmica
    public void irParaTelaPagamento(ComprarIngressoDTO carrinhoDTO) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(TELA_PAGAMENTO));
            Parent root = loader.load();

            // Injeção de dados via controller
             PagamentoController controller = loader.getController();
             controller.initData(carrinhoDTO);

            // Troca o miolo do BorderPane se estiver configurado, ou a cena inteira
            if (painelPrincipal != null) {
                painelPrincipal.setCenter(root);
            } else {
                stagePrincipal.getScene().setRoot(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // AlertManager.exibirAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível carregar o pagamento.");
        }
    }

    private static final String TELA_LOGIN = "/org/hexanet/eventhub/auth/Login.fxml";

    public void abrirLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(TELA_LOGIN));
            Parent root = loader.load();

            if (painelPrincipal != null) {
                painelPrincipal.setCenter(root);
            } else {
                stagePrincipal.getScene().setRoot(root);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static final String TELA_CADASTRO = "/org/hexanet/eventhub/auth/Cadastro.fxml";
    public void abrirCadastro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(TELA_CADASTRO));
            Parent root = loader.load();

            if (painelPrincipal != null) {
                painelPrincipal.setCenter(root);
            } else {
                stagePrincipal.getScene().setRoot(root);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}