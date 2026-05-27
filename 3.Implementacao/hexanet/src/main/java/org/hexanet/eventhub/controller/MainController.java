package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.hexanet.eventhub.model.Organizador;
import org.hexanet.eventhub.model.Usuario;
import org.hexanet.eventhub.singleton.ScreenManager;
import org.hexanet.eventhub.singleton.SessaoUsuario;
import javafx.scene.Node;

public class MainController {
    @FXML private BorderPane mainContainer;
    @FXML private StackPane contentArea;

    @FXML private HBox barraPesquisa;
    @FXML private HBox menuDeslogado;
    @FXML private HBox menuLogado;

    @FXML private Label lblSaudacao;
    @FXML private Button btnGerenciarEventos;
    @FXML private Button btnGerenciarPerfil;
    public void initialize() {
        // Registra o painel central no Singleton de Navegação
        ScreenManager.getInstancia().setPainelPrincipal(mainContainer);
        ScreenManager.getInstancia().setMainController(this);
    }

    public void atualizarMenu(String nomeTelaAtual) {
        Usuario usuarioLogado = SessaoUsuario.getInstancia().getUsuarioLogado();

        boolean isTelaInicio = nomeTelaAtual.equals("ConsultarEventos");
        alternarVisibilidade(barraPesquisa, isTelaInicio);

        // 2. Controle dos Menus Baseado em Login
        if (usuarioLogado == null) {
            // Deslogado
            alternarVisibilidade(menuDeslogado, true);
            alternarVisibilidade(menuLogado, false);
        } else {
            // Logado
            alternarVisibilidade(menuDeslogado, false);
            alternarVisibilidade(menuLogado, true);

            // Personaliza a saudação
            lblSaudacao.setText("Olá, " + usuarioLogado.getNome());

            // 3. Controle de Nível de Permissão (Participante vs Administrador/Organizador)
            if (usuarioLogado instanceof Organizador) {
                alternarVisibilidade(btnGerenciarEventos, true);
            } else { // Assume que é Participante
                alternarVisibilidade(btnGerenciarEventos, false);
            }
        }
    }

    private void alternarVisibilidade(Node node, boolean visivel) {
        node.setVisible(visivel);
        node.setManaged(visivel);
    }

    public void atualizarMenu() {
        atualizarMenu("");
    }

    public void exibirBarraPesquisa(boolean mostrar) {
        alternarVisibilidade(barraPesquisa, mostrar);
    }


    // --- Ações de Navegação ---

    @FXML
    public void irParaInicio() {
        ScreenManager.getInstancia().irParaConsultarEventos();
        atualizarMenu("ConsultarEventos");
    }

    @FXML
    public void irParaLogin() {
        ScreenManager.getInstancia().irParaLogin();
        atualizarMenu("Login");
    }

    @FXML
    public void irParaGerenciarPerfil() {
        ScreenManager.getInstancia().irParaGerenciarPerfil();
        atualizarMenu("GerenciarPerfil");
    }

    @FXML
    public void irParaGerenciarEventos() {
        ScreenManager.getInstancia().irParaGerenciarEventos();
        atualizarMenu("GerenciarEventos");
    }

    @FXML
    public void fazerLogout() {
        SessaoUsuario.getInstancia().logout();
        irParaInicio(); // Redireciona para o início deslogado
    }
    @FXML
    public void voltar() {
        ScreenManager.getInstancia().voltarTelaAnterior();
        atualizarMenu("");
    }
}
