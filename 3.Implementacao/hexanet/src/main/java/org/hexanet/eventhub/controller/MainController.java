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
        DetalhesEventoDTO detalhes = gerarDetalhes();

        atualizarMenu();
        exibirBarraPesquisa(false);

        ScreenManager.getInstancia().irParaTelaComprarIngressos(detalhes);
    }

    public void irParaLogin() {
        ScreenManager.getInstancia().abrirLogin();
    }

    public DetalhesEventoDTO gerarDetalhes() {
        DetalhesEventoDTO mockDTO = new DetalhesEventoDTO();
        mockDTO.setIdEvento(99L);
        mockDTO.setNome("Festa Junina Teste (MOCK)");
        mockDTO.setLocal("Pavilhão Central");
        mockDTO.setDataHora(LocalDateTime.now().plusDays(10)); // Evento daqui a 10 dias

        // Criar opções de ingressos falsas
        List<TipoIngressoDTO> tiposMocks = new ArrayList<>();

        TipoIngressoDTO tipo1 = new TipoIngressoDTO();
        tipo1.setId(1L);
        tipo1.setNome("VIP - 1º Lote");
        tipo1.setPreco(150.50);
        tipo1.setQtdDisponiveis(10); // Máximo que o spinner vai aceitar

        TipoIngressoDTO tipo2 = new TipoIngressoDTO();
        tipo2.setId(2L);
        tipo2.setNome("Pista - 2º Lote");
        tipo2.setPreco(50.00);
        tipo2.setQtdDisponiveis(5);

        tiposMocks.add(tipo1);
        tiposMocks.add(tipo2);

        mockDTO.setTiposDisponiveis(tiposMocks);

        return  mockDTO;
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

//    @FXML
//    public void irParaEventos() {
////        ScreenManager.getInstancia().
//    }
}
