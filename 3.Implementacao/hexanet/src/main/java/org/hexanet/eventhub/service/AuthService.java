package org.hexanet.eventhub.service;

import org.hexanet.eventhub.dao.ParticipanteDAO;
import org.hexanet.eventhub.dto.CadastroUsuarioDTO;
import org.hexanet.eventhub.exceptions.SenhaInvalidaException;
import org.hexanet.eventhub.model.Participante;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    ParticipanteDAO participanteDAO = new ParticipanteDAO();

    public void cadastrar(CadastroUsuarioDTO dto) {
        validarSenha(dto.getSenha(), dto.getConfirmarSenha());

        Participante usuario = new Participante();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());
        usuario.getDataNasc(dto.getDataNasc());
        usuario.setSenhaHash(criptografarSenha(dto.getSenha()));

        participanteDAO.salvar(usuario);

    }

    private void validarSenha(String senha, String confirmarSenha) {
        if (!senha.equals(confirmarSenha)) {
            throw new SenhaInvalidaException("As senhas devem ser iguais");
        } else if(senha.isBlank()) {
            throw new SenhaInvalidaException("A senha não pode ser vazia!");
        } else if(senha.length() < 8) {
            throw new SenhaInvalidaException("A senha deve ter no mínimo 8 caracteres");
        }
        String sequencias[] = {"123", "abc", "qwerty"};
        for (String sequencia : sequencias) {
            if(senha.toLowerCase().contains(sequencia)) {
                throw new SenhaInvalidaException("Senha não dever conter sequências");
            }
        }

    }

    private String criptografarSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }

}
