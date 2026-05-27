package org.hexanet.eventhub.singleton;

import org.hexanet.eventhub.model.Organizador;
import org.hexanet.eventhub.model.Usuario;

public class SessaoUsuario {
    private static SessaoUsuario instancia;
    private Usuario usuarioLogado;

    private SessaoUsuario() {}

    public static SessaoUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SessaoUsuario();
        }
        return instancia;
    }
    public void login(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public void logout() {
        ScreenManager.getInstancia().limparHistorico();
        this.usuarioLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public boolean isLogado() {
        return usuarioLogado != null;
    }
    public boolean isOrganizador() {
        return usuarioLogado instanceof Organizador;
    }
}
