package org.hexanet.eventhub.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.hexanet.eventhub.dto.UsuarioDTO;
import org.hexanet.eventhub.exceptions.CampoInvalidoException;
import org.hexanet.eventhub.model.Organizador;
import org.hexanet.eventhub.model.Participante;
import org.hexanet.eventhub.model.Usuario;
import org.hexanet.eventhub.service.GerenciarPerfilService;
import org.hexanet.eventhub.singleton.ScreenManager;
import org.hexanet.eventhub.singleton.SessaoUsuario;

public class GerenciarPerfilController {

    // ── Campos Comuns ────────────────────────────────────────────────────────
    @FXML private Label       lblSubtitulo;
    @FXML private TextField   txtNome;
    @FXML private Label       lblErroNome;
    @FXML private TextField   txtEmail;
    @FXML private Label       lblErroEmail;

    // ── Campos Condicionais ──────────────────────────────────────────────────
    @FXML private VBox        vboxDataNasc;
    @FXML private DatePicker  dpDataNasc;
    @FXML private Label       lblErroData;

    @FXML private VBox        vboxDocumento;
    @FXML private Label       lblDocumento;
    @FXML private TextField   txtDocumento;
    @FXML private Label       lblErroDocumento;

    // ── Campos de Senha ──────────────────────────────────────────────────────
    @FXML private PasswordField txtNovaSenha;
    @FXML private TextField     txtNovaSenhaRevelada;
    @FXML private Label         lblErroSenha;
    @FXML private CheckBox chkMostrarSenha;
    @FXML private PasswordField txtConfirmarSenha;
    @FXML private TextField     txtConfirmarSenhaRevelada;
    @FXML private Label         lblErroConfirmarSenha;

    // ── Feedback ─────────────────────────────────────────────────────────────
    @FXML private Label lblMensagem;

    // ── Serviço ──────────────────────────────────────────────────────────────
    private final GerenciarPerfilService service = new GerenciarPerfilService();

    // ── Inicialização ─────────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        Usuario usuario = SessaoUsuario.getInstancia().getUsuarioLogado();
        if (usuario == null) return;

        // Pré-preenche os campos comuns
        txtNome.setText(usuario.getNome());
        txtEmail.setText(usuario.getEmail());

        // Configura campos específicos por tipo de usuário
        if (usuario instanceof Participante participante) {
            lblSubtitulo.setText("Atualize suas informações de participante");

            vboxDataNasc.setVisible(true);
            vboxDataNasc.setManaged(true);
            if (participante.getDataNasc(null) != null) {
                dpDataNasc.setValue(participante.getDataNasc(null));
            }

            vboxDocumento.setVisible(true);
            vboxDocumento.setManaged(true);
            lblDocumento.setText("CPF");
            txtDocumento.setPromptText("000.000.000-00");
            if (participante.getCpf() != null) {
                txtDocumento.setText(participante.getCpf());
            }

        } else if (usuario instanceof Organizador organizador) {
            lblSubtitulo.setText("Atualize suas informações de organizador");

            vboxDocumento.setVisible(true);
            vboxDocumento.setManaged(true);
            lblDocumento.setText("CNPJ");
            txtDocumento.setPromptText("00.000.000/0000-00");
            if (organizador.getCnpj() != null) {
                txtDocumento.setText(organizador.getCnpj());
            }
        }

        // Sincroniza os campos de texto revelado com os de senha
        sincronizarSenhaComRevelada(txtNovaSenha, txtNovaSenhaRevelada);
        sincronizarSenhaComRevelada(txtConfirmarSenha, txtConfirmarSenhaRevelada);
    }

    // ── Ações ─────────────────────────────────────────────────────────────────

    @FXML
    private void salvarAlteracoes() {
        limparErros();

        if (!validarCampos()) return;

        try {
            UsuarioDTO dto = montarDTO();
            service.atualizarPerfil(dto);

            exibirSucesso("Perfil atualizado com sucesso!");
            limparCamposSenha();

        } catch (CampoInvalidoException e) {
            exibirErroGeral(e.getMessage());
        } catch (Exception e) {
            exibirErroGeral("Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        ScreenManager.getInstancia().voltarTelaAnterior();
    }

    @FXML
    private void toggleMostrarSenha() {
        boolean mostrar = chkMostrarSenha.isSelected();

        txtNovaSenha.setVisible(!mostrar);
        txtNovaSenha.setManaged(!mostrar);
        txtNovaSenhaRevelada.setVisible(mostrar);
        txtNovaSenhaRevelada.setManaged(mostrar);

        txtConfirmarSenha.setVisible(!mostrar);
        txtConfirmarSenha.setManaged(!mostrar);
        txtConfirmarSenhaRevelada.setVisible(mostrar);
        txtConfirmarSenhaRevelada.setManaged(mostrar);
    }

    // ── Helpers Internos ──────────────────────────────────────────────────────

    private UsuarioDTO montarDTO() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome(txtNome.getText().trim());
        dto.setEmail(txtEmail.getText().trim());

        // Campos condicionais
        if (vboxDataNasc.isVisible()) {
            dto.setDataNasc(dpDataNasc.getValue());
        }
        if (vboxDocumento.isVisible()) {
            dto.setCpfOrCnpj(txtDocumento.getText().trim());
        }

        // Senha (pode estar em branco — o serviço trata isso)
        String novaSenha = txtNovaSenha.isVisible()
                ? txtNovaSenha.getText()
                : txtNovaSenhaRevelada.getText();
        String confirmar = txtConfirmarSenha.isVisible()
                ? txtConfirmarSenha.getText()
                : txtConfirmarSenhaRevelada.getText();

        dto.setSenha(novaSenha);
        dto.setConfirmarSenha(confirmar);

        return dto;
    }

    /** Valida os campos visuais antes de chamar o serviço. Retorna true se tudo ok. */
    private boolean validarCampos() {
        boolean valido = true;

        if (txtNome.getText().isBlank()) {
            exibirErro(lblErroNome, "O nome não pode ser vazio.");
            valido = false;
        }
        if (txtEmail.getText().isBlank()) {
            exibirErro(lblErroEmail, "O e-mail não pode ser vazio.");
            valido = false;
        }
        if (vboxDataNasc.isVisible() && dpDataNasc.getValue() == null) {
            exibirErro(lblErroData, "Selecione a data de nascimento.");
            valido = false;
        }

        return valido;
    }

    private void limparErros() {
        esconderLabel(lblErroNome);
        esconderLabel(lblErroEmail);
        esconderLabel(lblErroData);
        esconderLabel(lblErroDocumento);
        esconderLabel(lblErroSenha);
        esconderLabel(lblErroConfirmarSenha);
        esconderLabel(lblMensagem);
    }

    private void limparCamposSenha() {
        txtNovaSenha.clear();
        txtNovaSenhaRevelada.clear();
        txtConfirmarSenha.clear();
        txtConfirmarSenhaRevelada.clear();
    }

    private void exibirErro(Label label, String mensagem) {
        label.setText(mensagem);
        label.setVisible(true);
        label.setManaged(true);
    }

    private void exibirErroGeral(String mensagem) {
        lblMensagem.setText(mensagem);
        lblMensagem.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 13px; -fx-font-weight: bold;");
        lblMensagem.setVisible(true);
        lblMensagem.setManaged(true);
    }

    private void exibirSucesso(String mensagem) {
        lblMensagem.setText(mensagem);
        lblMensagem.setStyle("-fx-text-fill: #10B981; -fx-font-size: 13px; -fx-font-weight: bold;");
        lblMensagem.setVisible(true);
        lblMensagem.setManaged(true);
    }

    private void esconderLabel(Label label) {
        label.setVisible(false);
        label.setManaged(false);
        label.setText("");
    }

    /** Alterna entre PasswordField e TextField para mostrar/ocultar senha. */
    private void alternarVisibilidadeSenha(PasswordField campo, TextField revelado) {
        if (campo.isVisible()) {
            revelado.setText(campo.getText());
            campo.setVisible(false);
            campo.setManaged(false);
            revelado.setVisible(true);
            revelado.setManaged(true);
        } else {
            campo.setText(revelado.getText());
            revelado.setVisible(false);
            revelado.setManaged(false);
            campo.setVisible(true);
            campo.setManaged(true);
        }
    }

    /** Mantém PasswordField e TextField sincronizados ao digitar. */
    private void sincronizarSenhaComRevelada(PasswordField campo, TextField revelado) {
        campo.textProperty().addListener((obs, antigo, novo) -> {
            if (campo.isVisible()) revelado.setText(novo);
        });
        revelado.textProperty().addListener((obs, antigo, novo) -> {
            if (revelado.isVisible()) campo.setText(novo);
        });
    }
}