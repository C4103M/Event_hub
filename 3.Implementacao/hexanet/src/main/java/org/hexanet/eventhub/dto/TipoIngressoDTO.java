package org.hexanet.eventhub.dto;

public class TipoIngressoDTO {
    private Long id;
    private String nome;
    private String descricaoCurta;
    private double preco;
    private int qtdDisponiveis;

    public TipoIngressoDTO(Long id, String nome, double preco, int qtdDisponiveis) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.qtdDisponiveis = qtdDisponiveis;
    }

    public TipoIngressoDTO() {

    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
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

    public String getDescricaoCurta() {
        return descricaoCurta;
    }

    public void setDescricaoCurta(String descricaoCurta) {
        this.descricaoCurta = descricaoCurta;
    }

    public int getQtdDisponiveis() {
        return qtdDisponiveis;
    }

    public void setQtdDisponiveis(int qtdDisponiveis) {
        this.qtdDisponiveis = qtdDisponiveis;
    }



}