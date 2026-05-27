package org.hexanet.eventhub.dto;

import java.time.LocalDate;

public class UsuarioDTO {
    private String nome;
    private String email;
    private String cpf;
    private LocalDate dataNasc;
    private String senha;
    private String confirmarSenha;
    private String tipoUsuario;

    public UsuarioDTO(String nome, String email, String cpf, LocalDate dataNasc, String senha, String confirmarSenha) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.dataNasc = dataNasc;
        this.senha = senha;
        this.confirmarSenha = confirmarSenha;
    }
    public UsuarioDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }


    public UsuarioDTO() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(LocalDate dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
