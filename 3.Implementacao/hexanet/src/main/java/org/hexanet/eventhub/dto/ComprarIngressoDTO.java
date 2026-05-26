package org.hexanet.eventhub.dto;

import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.enums.MetodoPagamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
public class ComprarIngressoDTO {
    private Long idEvento;
    private String nomeEvento;
    private double valorTotalPedido;

    // Dados vitais para persistência no banco
    private List<Ingresso> ingressosSelecionados;
    private MetodoPagamento metodoPagamento;

    public ComprarIngressoDTO() {
    }

    public ComprarIngressoDTO(Long idEvento, String nomeEvento, double valorTotalPedido, List<Ingresso> ingressosSelecionados, MetodoPagamento metodoPagamento) {
        this.idEvento = idEvento;
        this.nomeEvento = nomeEvento;
        this.valorTotalPedido = valorTotalPedido;
        this.ingressosSelecionados = ingressosSelecionados;
        this.metodoPagamento = metodoPagamento;
    }
    public ComprarIngressoDTO(List<Ingresso> ingressos, MetodoPagamento metodoPagamento) {
        this.ingressosSelecionados = ingressos;
        this.metodoPagamento = metodoPagamento;
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public double getValorTotalPedido() {
        return valorTotalPedido;
    }

    public void setValorTotalPedido(double valorTotalPedido) {
        this.valorTotalPedido = valorTotalPedido;
    }

    public List<Ingresso> getIngressosSelecionados() {
        return ingressosSelecionados;
    }

    public void setIngressosSelecionados(List<Ingresso> ingressosSelecionados) {
        this.ingressosSelecionados = ingressosSelecionados;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}
