package org.hexanet.eventhub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "participante")
public class Participante extends Usuario {
    private String cpf;

    @Column(name = "data_nasc")
    private LocalDate dataNasc;

    @OneToMany(mappedBy = "participante")
    private List<Pedido> pedidos = new ArrayList<>();

    public Participante() {}

    public Participante(String nome, String email, String cpf, LocalDate dataNasc, String senhaHash) {
        super(nome, email, senhaHash);
        this.cpf = cpf;
        this.dataNasc = dataNasc;
    }


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNasc(LocalDate dataNasc) {
        return this.dataNasc;
    }

    public void setDataNasc(LocalDate dataNasc) {
        this.dataNasc = dataNasc;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public void addPedido(Pedido pedido) {
        this.pedidos.add(pedido);
    }

    public Pedido getPedido(int indice) {
        return this.pedidos.get(indice);
    }
}
