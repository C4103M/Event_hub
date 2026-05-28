package org.hexanet.eventhub.service;

import org.hexanet.eventhub.dao.UsuarioDAO;
import org.hexanet.eventhub.dto.UsuarioDTO;
import org.hexanet.eventhub.exceptions.CampoInvalidoException;
import org.hexanet.eventhub.model.Organizador;
import org.hexanet.eventhub.model.Participante;
import org.hexanet.eventhub.model.Usuario;
import org.hexanet.eventhub.singleton.SessaoUsuario;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;

public class GerenciarPerfilService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final AuthService authService = new AuthService();

    /**
     * Atualiza o perfil do usuário logado com os dados do DTO.
     * Campos de senha são opcionais: se ambos estiverem em branco, a senha não é alterada.
     */
    public void atualizarPerfil(UsuarioDTO dto) {
        Usuario usuario = SessaoUsuario.getInstancia().getUsuarioLogado();
        if (usuario == null) {
            throw new CampoInvalidoException("Nenhum usuário logado.");
        }

        validarNome(dto.getNome());
        validarEmail(dto.getEmail(), usuario);

        // Atualiza campos comuns
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());

        // Atualiza campos específicos por tipo
        if (usuario instanceof Participante participante) {
            if (dto.getDataNasc() == null) {
                throw new CampoInvalidoException("A data de nascimento não pode ser vazia.");
            }
            validarDataNasc(dto.getDataNasc());
            participante.setDataNasc(dto.getDataNasc());
            if (dto.getCpfOrCnpj() != null && !dto.getCpfOrCnpj().isBlank()) {
                participante.setCpf(dto.getCpfOrCnpj().replaceAll("\\D", ""));
            }

        } else if (usuario instanceof Organizador organizador) {
            if (dto.getCpfOrCnpj() != null && !dto.getCpfOrCnpj().isBlank()) {
                organizador.setCnpj(dto.getCpfOrCnpj().replaceAll("\\D", ""));
            }
        }

        // Atualiza senha somente se os campos forem preenchidos
        String novaSenha = dto.getSenha();
        String confirmarSenha = dto.getConfirmarSenha();

        boolean senhaInformada = novaSenha != null && !novaSenha.isBlank();
        boolean confirmarInformada = confirmarSenha != null && !confirmarSenha.isBlank();

        if (senhaInformada || confirmarInformada) {
            validarSenha(novaSenha, confirmarSenha);
            usuario.setSenhaHash(authService.criptografarSenha(novaSenha));
        }

        usuarioDAO.atualizar(usuario);

        // Atualiza a sessão com os dados mais recentes
        SessaoUsuario.getInstancia().login(usuario);
    }

    // ── Validações ─────────────────────────────────────────────────────────────

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new CampoInvalidoException("O nome completo não pode ser vazio.");
        }
        if (nome.trim().length() < 3) {
            throw new CampoInvalidoException("O nome deve ter ao menos 3 caracteres.");
        }
    }

    private void validarEmail(String email, Usuario usuarioAtual) {
        if (email == null || email.isBlank()) {
            throw new CampoInvalidoException("O e-mail não pode ser vazio.");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new CampoInvalidoException("E-mail inválido.");
        }
        // Verifica se o novo e-mail já está em uso por outro usuário
        if (!email.equalsIgnoreCase(usuarioAtual.getEmail())) {
            Usuario existente = usuarioDAO.buscarPorEmail(email);
            if (existente != null && !existente.getId().equals(usuarioAtual.getId())) {
                throw new CampoInvalidoException("Este e-mail já está cadastrado.");
            }
        }
    }

    private void validarSenha(String senha, String confirmar) {
        if (senha == null || senha.isBlank()) {
            throw new CampoInvalidoException("A nova senha não pode ser vazia.");
        }
        if (senha.length() < 8) {
            throw new CampoInvalidoException("A senha deve ter no mínimo 8 caracteres.");
        }
        String[] sequencias = {"123", "abc", "qwerty"};
        for (String seq : sequencias) {
            if (senha.toLowerCase().contains(seq)) {
                throw new CampoInvalidoException("A senha não deve conter sequências óbvias.");
            }
        }
        if (!senha.equals(confirmar)) {
            throw new CampoInvalidoException("As senhas não coincidem.");
        }
    }

    private void validarDataNasc(LocalDate data) {
        if (data.isAfter(LocalDate.now())) {
            throw new CampoInvalidoException("A data de nascimento não pode ser futura.");
        }
        if (data.isAfter(LocalDate.now().minusYears(10))) {
            throw new CampoInvalidoException("Idade mínima de 10 anos.");
        }
    }
}