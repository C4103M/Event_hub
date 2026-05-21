package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hexanet.eventhub.dto.UsuarioDTO;
import org.hexanet.eventhub.exceptions.SenhaInvalidaException;
import org.hexanet.eventhub.exceptions.UsuarioNaoEncontradoException;
import org.hexanet.eventhub.service.AuthService;
import org.hexanet.eventhub.utils.AlertManager;
import org.hexanet.eventhub.utils.ScreenManager;

public class AuthController {
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCPF;
    @FXML private DatePicker dpDataNasc;
    @FXML private PasswordField txtSenha;
    @FXML private PasswordField txtConfirmarSenha;
    @FXML private CheckBox chkMostrarSenha;

    AuthService authService = new AuthService();

    @FXML
    public void cadastrar() {
        UsuarioDTO usuario = new UsuarioDTO(
                txtNome.getText(),
                txtEmail.getText(),
                txtCPF.getText(),
                dpDataNasc.getValue(),
                txtSenha.getText(),
                txtConfirmarSenha.getText()
        );

        try {
            authService.cadastrar(usuario);
        } catch (SenhaInvalidaException e) {
            AlertManager.exibirAlerta(Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
            txtSenha.clear();
            txtConfirmarSenha.clear();
            txtSenha.requestFocus();
        } catch (Exception e) {
            AlertManager.exibirAlerta(Alert.AlertType.ERROR, "Erro Interno", "Ocorreu um erro ao salvar no banco de dados.");
        }

    }

    public void logar() {
        UsuarioDTO usuario = new UsuarioDTO(txtEmail.getText(), txtSenha.getText());
        try {
            this.authService.logar(usuario);
        } catch (UsuarioNaoEncontradoException | SenhaInvalidaException e) {
            AlertManager.exibirAlerta(Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
        } catch (Exception e) {
            AlertManager.exibirAlerta(Alert.AlertType.ERROR, "Erro Interno", e.getMessage());
        }

    }

    @FXML
    public void goLogin() {
        Stage stageJanelaAtual = (Stage) this.txtCPF.getScene().getWindow();
        ScreenManager.abrirLogin(stageJanelaAtual);
    }
    @FXML
    public void goCadastro() {
        Stage stageJanelaAtual = (Stage) this.txtCPF.getScene().getWindow();
        ScreenManager.abrirLogin(stageJanelaAtual);
    }

    public void toogleMostrarSenha() {
        if(chkMostrarSenha.isSelected()) {

        }
    }


}
