package org.hexanet.eventhub.service;

import org.hexanet.eventhub.dao.ParticipanteDAO;
import org.hexanet.eventhub.dao.UsuarioDAO;
import org.hexanet.eventhub.dto.UsuarioDTO;
import org.hexanet.eventhub.exceptions.CampoInvalidoException;
import org.hexanet.eventhub.exceptions.UsuarioNaoEncontradoException;
import org.hexanet.eventhub.model.Participante;
import org.hexanet.eventhub.model.Usuario;
import org.hexanet.eventhub.singleton.SessaoUsuario;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    ParticipanteDAO participanteDAO = new ParticipanteDAO();
    UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void cadastrar(UsuarioDTO dto) {
        validarSenha(dto.getSenha(), dto.getConfirmarSenha());

        Participante usuario = new Participante();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());
        usuario.getDataNasc(dto.getDataNasc());
        usuario.setSenhaHash(criptografarSenha(dto.getSenha()));

        participanteDAO.salvar(usuario);

    }

    public void logar(UsuarioDTO dto) {
        Usuario usuarioLogado = this.usuarioDAO.buscarPorEmail(dto.getEmail());
        if(usuarioLogado == null) {
            throw new UsuarioNaoEncontradoException("Usuário não Encontrado. Verifique o Email e tente novamente");
        }
        if(! this.compararSenha(dto.getSenha(), usuarioLogado.getSenhaHash())) {
            throw new CampoInvalidoException("Senha inválida");
        }
        SessaoUsuario.getInstancia().login(usuarioLogado);

    }

    private void validarSenha(String senha, String confirmarSenha) {
        if (!senha.equals(confirmarSenha)) {
            throw new CampoInvalidoException("As senhas devem ser iguais");
        } else if(senha.isBlank()) {
            throw new CampoInvalidoException("A senha não pode ser vazia!");
        } else if(senha.length() < 8) {
            throw new CampoInvalidoException("A senha deve ter no mínimo 8 caracteres");
        }
        String[] sequencias = {"123", "abc", "qwerty"};
        for (String sequencia : sequencias) {
            if(senha.toLowerCase().contains(sequencia)) {
                throw new CampoInvalidoException("Senha não dever conter sequências");
            }
        }
    }
    private void validarCPF(String cpf) {
        if (cpf == null) {
            throw new CampoInvalidoException("CPF Não pode ser vazio");
        }
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            throw new CampoInvalidoException("CPF Não pode ser uma sequência de numeros");
        }

        try {
            // Cálculo do 1º Dígito Verificador
            int soma = 0;
            int peso = 10;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }
            int resto = 11 - (soma % 11);
            char digito1 = (resto == 10 || resto == 11) ? '0' : (char) (resto + '0');

            // Cálculo do 2º Dígito Verificador
            soma = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * peso--;
            }
            resto = 11 - (soma % 11);
            char digito2 = (resto == 10 || resto == 11) ? '0' : (char) (resto + '0');

            // Valida se os dígitos calculados batem com os dígitos informados
            if(!( digito1 == cpf.charAt(9) && digito2 == cpf.charAt(10))) {
                throw new CampoInvalidoException("CPF Inválido");
            }

        } catch (Exception e) {
            throw new CampoInvalidoException("Erro ao validar CPF");
        }
    }
    private void validarEmail(String email) {
        if(!email.contains("@")) {
            throw new CampoInvalidoException("Email Inválido");
        }
    }


    public boolean compararSenha(String senha, String senhaHash) {
        return BCrypt.checkpw(senha, senhaHash);
    }

    public String criptografarSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }

}
