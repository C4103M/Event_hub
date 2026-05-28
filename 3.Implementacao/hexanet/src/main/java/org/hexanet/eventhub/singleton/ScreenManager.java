package org.hexanet.eventhub.singleton;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.hexanet.eventhub.controller.ComprarIngressoController;
import org.hexanet.eventhub.controller.MainController;
import org.hexanet.eventhub.controller.PagamentoController;
import org.hexanet.eventhub.dto.ComprarIngressoDTO;
import org.hexanet.eventhub.dto.DetalhesEventoDTO;
import org.hexanet.eventhub.exceptions.PermissaoNegadaException;

import org.hexanet.eventhub.utils.AlertManager;

import java.io.IOException;
import java.util.Stack;

public class ScreenManager {

    private static ScreenManager instancia;
    private Stage stagePrincipal;
    private BorderPane painelPrincipal;
    private MainController mainController;
    private Stack<String> historicoTelas = new Stack<>();

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

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private static final String MAIN_LAYOUT = "/org/hexanet/eventhub/MainLayout.fxml";
    public void carregarLayoutPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_LAYOUT));
            Parent root = loader.load();

            this.mainController = loader.getController();

            this.painelPrincipal = (BorderPane) root;

            Scene scene = new Scene(root);
            this.stagePrincipal.setScene(scene);
            irParaConsultarEventos();
            this.stagePrincipal.show();


        } catch (Exception e) {
            throw new RuntimeException("Exceção ao carregar o layout no ScreenManager: " + e.getMessage(), e);
        }
    }

    // ------------- Métodos de navegação ------------------
    private static final String TELA_CONSULTAR_EVENTOS = "/org/hexanet/eventhub/eventos/ConsultarEventos.fxml";
    public void irParaConsultarEventos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(TELA_CONSULTAR_EVENTOS));
            Parent root = loader.load();

            // Troca o miolo do BorderPane se estiver configurado, ou a cena inteira
            if (painelPrincipal != null) {
                painelPrincipal.setCenter(root);
            } else {
                stagePrincipal.getScene().setRoot(root);
            }
            String nomeTela = "ConsultarEventos";
            this.mainController.atualizarMenu(false);

            if (historicoTelas.isEmpty() || !historicoTelas.peek().equals(nomeTela)) {
                historicoTelas.push(nomeTela);
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertManager.exibirAlerta(Alert.AlertType.ERROR, "ERRO", e.getMessage());
            throw new RuntimeException("Erro ao carregar a tela ConsultarEventos: " + e.getMessage(), e);
        }
    }


    private static final String TELA_FORMULARIO_EVENTO = "/org/hexanet/eventhub/eventos/FormularioEvento.fxml";
    public void irParaFormularioEvento(org.hexanet.eventhub.model.Evento eventoEdicao) {
        try {
            if(! SessaoUsuario.getInstancia().isOrganizador()) {
                throw new PermissaoNegadaException("Apenas organizadores pode acessar esta área");
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(TELA_FORMULARIO_EVENTO));
            Parent root = loader.load();

            if (eventoEdicao != null) {
                org.hexanet.eventhub.controller.ManterEventoController controller = loader.getController();
                controller.preencherFormulario(eventoEdicao);
            }

            this.mainController.atualizarMenu(false);

            if (painelPrincipal != null) {
                painelPrincipal.setCenter(root);
            } else {
                stagePrincipal.getScene().setRoot(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String TELA_COMPRAR_INGRESSO = "/org/hexanet/eventhub/ingressos/ComprarIngresso.fxml";
    public void irParaComprarIngressos(DetalhesEventoDTO detalhes) {
        try {
//            if(!SessaoUsuario.getInstancia().isLogado()) {
//                throw new PermissaoNegadaException("Precisa estar logado");
//            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(TELA_COMPRAR_INGRESSO));
            Parent root = loader.load();

            ComprarIngressoController controller = loader.getController();
            controller.initData(detalhes);

            if (painelPrincipal != null) {
                painelPrincipal.setCenter(root);
            } else {
                stagePrincipal.getScene().setRoot(root);
            }

            String nomeTela = "ComprarIngresso";

            this.mainController.atualizarMenu(false);

            if (historicoTelas.isEmpty() || !historicoTelas.peek().equals(nomeTela)) {
                historicoTelas.push(nomeTela);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String TELA_PAGAMENTO = "/org/hexanet/eventhub/ingressos/Pagamento.fxml";
    public void irParaPagamento(ComprarIngressoDTO comprarIngressoDTO) {
        try {
//            if(! SessaoUsuario.getInstancia().isLogado()) {
//                throw new PermissaoNegadaException("Precisa ter uma conta");
//            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(TELA_PAGAMENTO));
            Parent root = loader.load();

            // Injeção de dados via controller
            PagamentoController controller = loader.getController();
            controller.initData(comprarIngressoDTO);

            // Troca o miolo do BorderPane se estiver configurado, ou a cena inteira
            if (painelPrincipal != null) {
                painelPrincipal.setCenter(root);
            } else {
                stagePrincipal.getScene().setRoot(root);
            }

            this.mainController.atualizarMenu(false);
            String nomeTela = "Pagamento";
            if (historicoTelas.isEmpty() || !historicoTelas.peek().equals(nomeTela)) {
                historicoTelas.push(nomeTela);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String TELA_LOGIN = "/org/hexanet/eventhub/auth/Login.fxml";
    public void irParaLogin() {
        try {
            if(SessaoUsuario.getInstancia().isLogado()) {
                throw new PermissaoNegadaException("Você já está logado");
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(TELA_LOGIN));
            Parent root = loader.load();

            if (painelPrincipal != null) {
                painelPrincipal.setCenter(root);
            } else {
                stagePrincipal.getScene().setRoot(root);
            }
            String nomeTela = "Login";
            this.mainController.atualizarMenu(false);

            if (historicoTelas.isEmpty() || !historicoTelas.peek().equals(nomeTela)) {
                historicoTelas.push(nomeTela);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final String TELA_CADASTRO = "/org/hexanet/eventhub/auth/Cadastro.fxml";
    public void irParaCadastro() {
        try {
            if(SessaoUsuario.getInstancia().isLogado()) {
                throw new PermissaoNegadaException("Você já está logado");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(TELA_CADASTRO));
            Parent root = loader.load();

            if (painelPrincipal != null) {
                painelPrincipal.setCenter(root);
            } else {
                stagePrincipal.getScene().setRoot(root);
            }


            String nomeTela = "Cadastro";

            this.mainController.atualizarMenu(false);

            if (historicoTelas.isEmpty() || !historicoTelas.peek().equals(nomeTela)) {
                historicoTelas.push(nomeTela);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final String TELA_GERENCIAR_EVENTOS = "/org/hexanet/eventhub/eventos/GerenciarEventos.fxml";
    public void irParaGerenciarEventos() {
        try {
            if(! SessaoUsuario.getInstancia().isOrganizador()) {
                throw new PermissaoNegadaException("Página restrita à Organizadores");
            }

            this.mainController.atualizarMenu(false);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(TELA_GERENCIAR_EVENTOS));
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

    private static final String TELA_GERENCIAR_PERFIL = "/org/hexanet/eventhub/GerenciarPerfil.fxml";
    public void irParaGerenciarPerfil() {
        try {
            if(! SessaoUsuario.getInstancia().isLogado()) {
                throw new PermissaoNegadaException("Página restrita à usuários");
            }
            this.mainController.atualizarMenu(false);

            FXMLLoader loader = new FXMLLoader(getClass().getResource(TELA_GERENCIAR_PERFIL));
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

    public void voltarTelaAnterior() {
        if (historicoTelas.size() > 1) {
            historicoTelas.pop(); // Remove a tela atual da pilha
            String telaAnterior = historicoTelas.peek(); // Descobre qual é a anterior

            // Usa um Switch ou If para recarregar do zero a tela correta
            switch (telaAnterior) {
                case "ConsultarEventos":
                    irParaConsultarEventos();
                    break;
                case "GerenciarEventos":
                    irParaGerenciarEventos();
                    break;
                case "Login":
                    irParaLogin();
                    break;
                case "Cadastro":
                    irParaCadastro();
                    break;
                case "GerenciarPerfil":
                    irParaGerenciarPerfil();
                    break;
                // Os dois casos com parâmetros
                default:
                    irParaConsultarEventos();
                    break;
            }
        }
    }

    public void limparHistorico() {
        this.historicoTelas.clear();
    }
}