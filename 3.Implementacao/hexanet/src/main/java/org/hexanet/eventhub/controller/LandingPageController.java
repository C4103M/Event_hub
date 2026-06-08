package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import org.hexanet.eventhub.singleton.ScreenManager;

public class LandingPageController {



    @FXML
    public void irParaLogin() {
        ScreenManager.getInstancia().irParaLogin();
    }

    @FXML
    public void irParaEventos() {
        ScreenManager.getInstancia().irParaConsultarEventos();
    }
}
