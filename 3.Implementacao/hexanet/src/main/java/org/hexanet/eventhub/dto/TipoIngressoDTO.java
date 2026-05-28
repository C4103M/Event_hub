package org.hexanet.eventhub.dto;

public class TipoIngressoDTO {
    private Long id;
    private String nome;
    private double preco;
    private int qtdDisponiveis;

    private double valotTotal;
    private int qtdSelecionados;

    public TipoIngressoDTO(Long id, String nome, double preco, int qtdDisponiveis) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.qtdDisponiveis = qtdDisponiveis;
    }

    public TipoIngressoDTO() {

    }

    public double getValotTotal() {
        return valotTotal;
    }

    public void setValotTotal(double valotTotal) {
        this.valotTotal = valotTotal;
    }

    public double getPreco() {
        return preco;
    }
    public int getQtdSelecionados() {
        return qtdSelecionados;
    }

    public void setQtdSelecionados(int qtdSelecionados) {
        this.qtdSelecionados = qtdSelecionados;
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

    public int getQtdDisponiveis() {
        return qtdDisponiveis;
    }

    public void setQtdDisponiveis(int qtdDisponiveis) {
        this.qtdDisponiveis = qtdDisponiveis;
    }



}