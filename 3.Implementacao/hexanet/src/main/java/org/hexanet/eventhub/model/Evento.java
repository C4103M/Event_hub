package org.hexanet.eventhub.model;

import jakarta.persistence.*;
import org.hexanet.eventhub.model.enums.StatusEvento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evento")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String local;

    private String descricao;

    @Column(name = "capacidade_total")
    private int capacidadeTotal;

    @Column(name = "qtd_disponiveis")
    private int qtdDisponiveis;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(name = "evento_img")
    private String eventoImg;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_evento")
    private StatusEvento statusEvento;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TipoIngresso> tiposIngresso = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "organizador_id")
    private Organizador organizador;

    public Evento() {

    }

    public Evento(Long id, String nome, String local, int capacidadeTotal, LocalDateTime dataHora,
            StatusEvento statusEvento) {
        this.id = id;
        this.nome = nome;
        this.local = local;
        this.capacidadeTotal = capacidadeTotal;
        this.dataHora = dataHora;
        this.statusEvento = statusEvento;
    }

    public Evento(String nome, String local, int capacidadeTotal, LocalDateTime dataHora, StatusEvento statusEvento) {
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

    public int getQtdDisponiveis() {
        return qtdDisponiveis;
    }

    public void setQtdDisponiveis(int qtdDisponiveis) {
        this.qtdDisponiveis = qtdDisponiveis;
    }

    public void subtrairQtdDisponiveis(int qtd) {
        this.qtdDisponiveis -= qtd;
    }



    public Organizador getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Organizador organizador) {
        this.organizador = organizador;
    }

    public void setEventoImg(String eventoImg) {
        this.eventoImg = eventoImg;
    }

    public String getEventoImg() {
        return eventoImg;
    }

    public List<TipoIngresso> getTiposIngresso() {
        return tiposIngresso;
    }
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setTiposIngresso(List<TipoIngresso> tiposIngresso) {
        this.tiposIngresso = tiposIngresso;
    }

    public String getData() {
        if (dataHora == null) return "";
        return dataHora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getHorario() {
        if (dataHora == null) return "";
        return dataHora.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
    }

    public void addTipoIngresso(TipoIngresso tipoIngresso) {
        if (this.tiposIngresso == null) {
            this.tiposIngresso = new ArrayList<>();
        }
        this.tiposIngresso.add(tipoIngresso);
        tipoIngresso.setEvento(this);
    }
}
