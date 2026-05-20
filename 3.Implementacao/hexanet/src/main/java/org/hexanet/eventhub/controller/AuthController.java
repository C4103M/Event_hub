package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hexanet.eventhub.dto.CadastroUsuarioDTO;
import org.hexanet.eventhub.exceptions.SenhaInvalidaException;
import org.hexanet.eventhub.service.AuthService;

public class AuthController {
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCPF;
    @FXML private DatePicker dpDataNasc;
    @FXML private PasswordField txtSenha;
    @FXML private PasswordField txtConfirmarSenha;

    AuthService authService = new AuthService();

    @FXML
    public void cadastrar() {
        CadastroUsuarioDTO usuario = new CadastroUsuarioDTO(
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
            exibirAlerta(Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
            txtSenha.clear();
            txtConfirmarSenha.clear();
            txtSenha.requestFocus();
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro Interno", "Ocorreu um erro ao salvar no banco de dados.");
        }

    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
