package org.hexanet.eventhub.service;

import org.hexanet.eventhub.dao.ParticipanteDAO;
import org.hexanet.eventhub.dao.UsuarioDAO;
import org.hexanet.eventhub.dto.UsuarioDTO;
import org.hexanet.eventhub.exceptions.SenhaInvalidaException;
import org.hexanet.eventhub.exceptions.UsuarioNaoEncontradoException;
import org.hexanet.eventhub.model.Participante;
import org.hexanet.eventhub.model.Usuario;
import org.hexanet.eventhub.singleton.SessaoUsuario;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    ParticipanteDAO participanteDAO = new ParticipanteDAO();
    UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void cadastrar(UsuarioDTO dto) {
        validarFormatoSenha(dto.getSenha(), dto.getConfirmarSenha());

        Participante usuario = new Participante();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());
        usuario.getDataNasc(dto.getDataNasc());
        usuario.setSenhaHash(criptografarSenha(dto.getSenha()));

        participanteDAO.salvar(usuario);

    }

    public void logar(UsuarioDTO dto) {
//        System.out.println("==============================================TESTE NO SERVICE=======================================================");
//        System.out.println(dto.getEmail());
        Usuario usuarioLogado = this.usuarioDAO.buscarPorEmail(dto.getEmail());
        if(usuarioLogado == null) {
            throw new UsuarioNaoEncontradoException("Usuário não Encontrado. Verifique o Email e tente novamente");
        }
        if(! this.compararSenha(dto.getSenha(), usuarioLogado.getSenhaHash())) {
            throw new SenhaInvalidaException("Senha inválida");
        }
        SessaoUsuario.getInstancia().login(usuarioLogado);

    }

    private void validarFormatoSenha(String senha, String confirmarSenha) {
        if (!senha.equals(confirmarSenha)) {
            throw new SenhaInvalidaException("As senhas devem ser iguais");
        } else if(senha.isBlank()) {
            throw new SenhaInvalidaException("A senha não pode ser vazia!");
        } else if(senha.length() < 8) {
            throw new SenhaInvalidaException("A senha deve ter no mínimo 8 caracteres");
        }
        String[] sequencias = {"123", "abc", "qwerty"};
        for (String sequencia : sequencias) {
            if(senha.toLowerCase().contains(sequencia)) {
                throw new SenhaInvalidaException("Senha não dever conter sequências");
            }
        }

    }


    public boolean compararSenha(String senha, String senhaHash) {
        return BCrypt.checkpw(senha, senhaHash);
    }

    public String criptografarSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }

}
