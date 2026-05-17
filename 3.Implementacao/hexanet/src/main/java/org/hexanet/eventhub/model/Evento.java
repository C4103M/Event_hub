package org.hexanet.eventhub.model;

import jakarta.persistence.*;
import org.hexanet.eventhub.model.enums.StatusEvento;

import java.time.LocalDateTime;


@Entity
@Table(name = "evento")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String local;

    @Column(name = "capacidade_total")
    private int capacidadeTotal;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "valor_ingresso")
    private double valorIngresso;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_evento")
    private StatusEvento statusEvento;

    @ManyToOne
    @JoinColumn(name = "organizador_id")
    private Organizador organizador;


    public Evento() {
        
    }
    public Evento(Long id, String nome, String local, int capacidadeTotal, LocalDateTime dataHora, StatusEvento statusEvento) {
        this.id = id;
        this.nome = nome;
        this.local = local;
        this.capacidadeTotal = capacidadeTotal;
        this.dataHora = dataHora;
        this.statusEvento = statusEvento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getCapacidadeTotal() {
        return capacidadeTotal;
    }

    public void setCapacidadeTotal(int capacidadeTotal) {
        this.capacidadeTotal = capacidadeTotal;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusEvento getStatusEvento() {
        return statusEvento;
    }

    public void setStatusEvento(StatusEvento statusEvento) {
        this.statusEvento = statusEvento;
    }
}
