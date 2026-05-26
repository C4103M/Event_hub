package org.hexanet.eventhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_ingresso")
public class TipoIngresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private double preco;

    @Column(name = "qtd_disponiveis", nullable = false)
    private int qtdDisponiveis;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    // Construtores
    public TipoIngresso() {}

    public TipoIngresso(String nome, double preco, int qtdDisponiveis) {
        this.nome = nome;
        this.preco = preco;
        this.qtdDisponiveis = qtdDisponiveis;
    }

    // Getters e Seters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }
    public int getQtdDisponiveis() { return qtdDisponiveis; }
    public void setQtdDisponiveis(int qtdDisponiveis) { this.qtdDisponiveis = qtdDisponiveis; }
    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }
}