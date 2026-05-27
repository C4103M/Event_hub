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
import org.hexanet.eventhub.singleton.SessaoUsuario;

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
    @FXML
    private TextField txtSenhaRevelada;
    @FXML
    private CheckBox chkMostrarSenha;

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
    public void initialize() {
        if (txtSenha != null && txtSenhaRevelada != null) {
            txtSenhaRevelada.textProperty().bindBidirectional(txtSenha.textProperty());
        }
    }

    @FXML
    public void toogleMostrarSenha() {
        if (chkMostrarSenha != null && txtSenha != null && txtSenhaRevelada != null) {
            boolean mostrar = chkMostrarSenha.isSelected();
            txtSenha.setVisible(!mostrar);
            txtSenha.setManaged(!mostrar);
            txtSenhaRevelada.setVisible(mostrar);
            txtSenhaRevelada.setManaged(mostrar);
        }
    }

    @FXML
    public void cadastrar() {

        String tipo = rbOrganizador.isSelected() ? "ORGANIZADOR" : "PARTICIPANTE";

        UsuarioDTO usuario = new UsuarioDTO();
                usuario.setNome(txtNome.getText());
                usuario.setEmail(txtEmail.getText());
                usuario.setCpf(txtCpfCnpj.getText());

                if("PARTICIPANTE".equals(tipo)){
                    usuario.setDataNasc(dpDataNasc.getValue());
                }

                usuario.setSenha(txtSenha.getText());
                usuario.setConfirmarSenha(txtSenha.getText()); // Até ter o txtConfirmarSenha na tela
                usuario.setTipoUsuario(tipo);

        try {
            authService.cadastrar(usuario);
            AlertManager.exibirAlerta(Alert.AlertType.CONFIRMATION, "Sucesso", "Usuário Cadastrado com sucesso!");
            irParaLogin();
        } catch (CampoInvalidoException e) {
            AlertManager.exibirAlerta(Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
            txtSenha.clear();
            txtSenha.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
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
            ScreenManager.getInstancia().atualizarMenu();
            
            if (SessaoUsuario.getInstancia().isOrganizador()) {
                ScreenManager.getInstancia().irParaGerenciarEventos();
            } else {
                ScreenManager.getInstancia().irParaConsultarEventos();
            }

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
        if(this.txtCpfCnpj != null) {
            this.txtCpfCnpj.clear();
        }
        if(this.rbOrganizador.isSelected()) {
            this.txtCpfCnpj.setPromptText("Digite seu CNPJ");
            this.lblCpfCnpj.setText("CNPJ");
        
            this.vboxDataNasc.setVisible(false);
            this.vboxDataNasc.setManaged(false);
        } else{
            this.txtCpfCnpj.setPromptText("Digite seu CPF");
            this.lblCpfCnpj.setText("CPF");

            this.vboxDataNasc.setVisible(true);
            this.vboxDataNasc.setManaged(true);
        }
    }

}