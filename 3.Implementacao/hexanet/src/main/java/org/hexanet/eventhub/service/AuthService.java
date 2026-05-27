package org.hexanet.eventhub.service;


import org.hexanet.eventhub.dao.UsuarioDAO;
import org.hexanet.eventhub.dto.UsuarioDTO;
import org.hexanet.eventhub.exceptions.CampoInvalidoException;
import org.hexanet.eventhub.exceptions.UsuarioNaoEncontradoException;
import org.hexanet.eventhub.model.Organizador;
import org.hexanet.eventhub.model.Participante;
import org.hexanet.eventhub.model.Usuario;
import org.hexanet.eventhub.singleton.SessaoUsuario;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;

public class AuthService {
    UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void cadastrar(UsuarioDTO dto) {
//        validarNome(dto.getNome());
//        validarEmail(dto.getEmail());
//        validarSenha(dto.getSenha(), dto.getConfirmarSenha());

        if ("ORGANIZADOR".equals(dto.getTipoUsuario())) {
            String cnpj = dto.getCpfOrCnpj();

//            validarCNPJ(cnpj);

            Organizador organizador = new Organizador();
            organizador.setNome(dto.getNome());
            organizador.setEmail(dto.getEmail());
            organizador.setCnpj(cnpj);
            organizador.setSenhaHash(criptografarSenha(dto.getSenha()));

            usuarioDAO.salvar(organizador);
        } else {
            String cpf = dto.getCpfOrCnpj();

//            validarCPF(cpf);
//            validarDataNasc(dto.getDataNasc());

            Participante participante = new Participante();
            participante.setNome(dto.getNome());
            participante.setEmail(dto.getEmail());
            participante.setCpf(cpf);
            participante.setDataNasc(dto.getDataNasc());
            participante.setSenhaHash(criptografarSenha(dto.getSenha()));

            usuarioDAO.salvar(participante);

        }
    }

    public void logar(UsuarioDTO dto) {
        Usuario usuarioLogado = this.usuarioDAO.buscarPorEmail(dto.getEmail());
        if (usuarioLogado == null) {
            throw new UsuarioNaoEncontradoException("Usuário não Encontrado. Verifique o Email e tente novamente");
        }
        if (!this.compararSenha(dto.getSenha(), usuarioLogado.getSenhaHash())) {
            throw new CampoInvalidoException("Senha inválida");
        }
        SessaoUsuario.getInstancia().login(usuarioLogado);

    }

    private void validarSenha(String senha, String confirmarSenha) {
        if (!senha.equals(confirmarSenha)) {
            throw new CampoInvalidoException("As senhas devem ser iguais");
        } else if (senha.isBlank()) {
            throw new CampoInvalidoException("A senha não pode ser vazia!");
        } else if (senha.length() < 8) {
            throw new CampoInvalidoException("A senha deve ter no mínimo 8 caracteres");
        }
        String[] sequencias = {"123", "abc", "qwerty"};
        for (String sequencia : sequencias) {
            if (senha.toLowerCase().contains(sequencia)) {
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
            if (!(digito1 == cpf.charAt(9) && digito2 == cpf.charAt(10))) {
                throw new CampoInvalidoException("CPF Inválido");
            }

        } catch (Exception e) {
            throw new CampoInvalidoException("Erro ao validar CPF");
        }
    }

    private void validarEmail(String email) {
        if (!email.contains("@")) {
            throw new CampoInvalidoException("Email Inválido");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new CampoInvalidoException("O nome completo não pode ser vazio.");
        }
    }

    private void validarDataNasc(LocalDate dataNasc) {

    }

    public boolean compararSenha(String senha, String senhaHash) {
        return BCrypt.checkpw(senha, senhaHash);
    }

    public String criptografarSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt(12));
    }


    private void validarCNPJ(String cnpj) {
        if (cnpj == null) throw new CampoInvalidoException("CNPJ Não pode ser vazio");

        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) throw new CampoInvalidoException("CNPJ Inválido");

        try {
            int sm = 0;
            int peso = 2;
            for (int i = 11; i >= 0; i--) {
                int num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = (peso == 9) ? 2 : peso + 1;
            }
            int r = sm % 11;
            char dig13 = (r < 2) ? '0' : (char) ((11 - r) + 48);

            sm = 0;
            peso = 2;
            for (int i = 12; i >= 0; i--) {
                int num = (int) (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = (peso == 9) ? 2 : peso + 1;
            }
            r = sm % 11;
            char dig14 = (r < 2) ? '0' : (char) ((11 - r) + 48);

            if (!(dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13))) throw new CampoInvalidoException("CNPJ Inválido");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
