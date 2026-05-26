package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.hexanet.eventhub.dto.DetalhesEventoDTO;
import org.hexanet.eventhub.dto.TipoIngressoDTO;
import org.hexanet.eventhub.singleton.ScreenManager;
import org.hexanet.eventhub.singleton.SessaoUsuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML private BorderPane mainContainer;
    @FXML private HBox menuLogado;
    @FXML private HBox menuDeslogado;
    @FXML private HBox barraPesquisa;

    public void initialize() {
        // Registra o painel central no Singleton de Navegação
        ScreenManager.getInstancia().setPainelPrincipal(mainContainer);

        atualizarMenu();
        exibirBarraPesquisa(true);

        ScreenManager.getInstancia().irParaConsultarEventos();
    }

    public void irParaLogin() {
        ScreenManager.getInstancia().abrirLogin();
    }


    public void atualizarMenu() {
        boolean estaLogado = SessaoUsuario.getInstancia().getUsuarioLogado() != null;

        // Alterna visibilidade do menu
        menuLogado.setVisible(estaLogado);
        menuLogado.setManaged(estaLogado); // Remove do layout se invisível

        menuDeslogado.setVisible(!estaLogado);
        menuDeslogado.setManaged(!estaLogado);
    }
    public void exibirBarraPesquisa(boolean mostrar) {
        barraPesquisa.setVisible(mostrar);
        barraPesquisa.setManaged(mostrar);
    }
}
