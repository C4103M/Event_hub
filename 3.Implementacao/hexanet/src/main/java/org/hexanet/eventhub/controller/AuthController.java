package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.hexanet.eventhub.dto.UsuarioDTO;
import org.hexanet.eventhub.exceptions.CampoInvalidoException;
import org.hexanet.eventhub.exceptions.UsuarioNaoEncontradoException;
import org.hexanet.eventhub.service.AuthService;
import org.hexanet.eventhub.utils.AlertManager;
import org.hexanet.eventhub.singleton.ScreenManager;

public class AuthController {
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtCpfCnpj;
    @FXML
    private DatePicker dpDataNasc;
    @FXML
    private PasswordField txtSenha;
//    @FXML
//    private PasswordField txtConfirmarSenha;
//    @FXML
//    private CheckBox chkMostrarSenha;

    @FXML
    private RadioButton rbParticipante;
    @FXML
    private RadioButton rbOrganizador;
    @FXML
    private Label lblCpfCnpj;
    @FXML
    private VBox vboxDataNasc;
    AuthService authService = new AuthService();

    @FXML
    public void cadastrar() {
        UsuarioDTO usuario = new UsuarioDTO(
                txtNome.getText(),
                txtEmail.getText(),
                txtCpfCnpj.getText(),
                dpDataNasc.getValue(),
                txtSenha.getText(),
                // Estou repetindo enquanto ainda não tem isto no front, quando tiver o campo confirmarSenha troca por txtConfirmarSenha.getText()
                txtSenha.getText()
        );

        try {
            authService.cadastrar(usuario);
            AlertManager.exibirAlerta(Alert.AlertType.CONFIRMATION, "Sucesso", "Usuário Cadastrado com sucesso!");
            irParaLogin();
        } catch (CampoInvalidoException e) {
            AlertManager.exibirAlerta(Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
            txtSenha.clear();
//            txtConfirmarSenha.clear();
            txtSenha.requestFocus();
        } catch (Exception e) {
            AlertManager.exibirAlerta(Alert.AlertType.ERROR, "Erro Interno", "Ocorreu um erro ao salvar no banco de dados.");
        }

    }

    public void logar() {
        System.out.println("==================TESTE NO CONTROLLER =================");
        System.out.println(txtEmail.getText().trim().toLowerCase());
        UsuarioDTO usuario = new UsuarioDTO(txtEmail.getText().trim().toLowerCase(), txtSenha.getText());
        try {
            this.authService.logar(usuario);
            AlertManager.exibirAlerta(Alert.AlertType.CONFIRMATION, "Sucesso", "Usuário logado com sucesso");
            ScreenManager.getInstancia().irParaConsultarEventos();

        } catch (UsuarioNaoEncontradoException | CampoInvalidoException e) {
            AlertManager.exibirAlerta(Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
        } catch (Exception e) {
            AlertManager.exibirAlerta(Alert.AlertType.ERROR, "Erro Interno", e.getMessage());
        }

    }

    @FXML
    public void irParaLogin() {
        ScreenManager.getInstancia().irParaLogin();
    }

    @FXML
    public void irParaCadastro() {
        ScreenManager.getInstancia().irParaCadastro();
    }

    public void toogleOrgPart() {
        if(this.rbOrganizador.isSelected() && !this.rbParticipante.isSelected()) {
            // Tem que sumir os campos de participante
            this.txtCpfCnpj.setText("Digite seu CNPJ");
            this.lblCpfCnpj.setText("CNPJ");
            this.vboxDataNasc.setVisible(true);
        } else if(!this.rbOrganizador.isSelected() && this.rbParticipante.isSelected()) {
            // Tem que sumir os do organizadores e aparecer os do participante
            this.txtCpfCnpj.setText("Digite seu CPF");
            this.lblCpfCnpj.setText("CPF");
            this.vboxDataNasc.setVisible(true);
        }
    }

}