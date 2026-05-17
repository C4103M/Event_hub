package org.hexanet.eventhub.model;

import jakarta.persistence.*;
import org.hexanet.eventhub.model.enums.MetodoPagamento;
import org.hexanet.eventhub.model.enums.StatusPagamento;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double valor;
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodo;
    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento")
    private StatusPagamento statusPagamento;

    public Pagamento(int id, double valor, LocalDateTime dataHora, MetodoPagamento metodo,
            StatusPagamento statusPagamento) {
        this.id = id;
        this.valor = valor;
        this.dataHora = dataHora;
        this.metodo = metodo;
        this.statusPagamento = statusPagamento;
    }

    public Pagamento() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public MetodoPagamento getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPagamento metodo) {
        this.metodo = metodo;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

}
