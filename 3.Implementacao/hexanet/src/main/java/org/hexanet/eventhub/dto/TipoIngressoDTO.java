package org.hexanet.eventhub.dto;

public class TipoIngressoDTO {
    private Long id;
    private String nome;
    private double preco;

    public TipoIngressoDTO(Long id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
}