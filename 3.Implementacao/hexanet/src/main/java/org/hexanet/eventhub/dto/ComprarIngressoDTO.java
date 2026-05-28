package org.hexanet.eventhub.dto;

import org.hexanet.eventhub.model.Evento;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.enums.MetodoPagamento;
import java.util.List;

public class ComprarIngressoDTO {
    private Long idEvento;
    private String nomeEvento;
    private Evento evento; // Contém o objeto mapeado completo necessário para o Service
    private double valorTotalPedido;

    // Lista física exigida pelo Service (Ex: 3 objetos ingresso individuais)
    private List<Ingresso> ingressosSelecionados;

    // Lista de resumo para preencher a tabela do JavaFX de forma direta
    private List<ItemPedidoDTO> itensResumo;

    private MetodoPagamento metodoPagamento;

    // + Construtores, Getters e Setters
    public Long getIdEvento() { return idEvento; }
    public void setIdEvento(Long idEvento) { this.idEvento = idEvento; }
    public String getNomeEvento() { return nomeEvento; }
    public void setNomeEvento(String nomeEvento) { this.nomeEvento = nomeEvento; }
    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }
    public double getValorTotalPedido() { return valorTotalPedido; }
    public void setValorTotalPedido(double valorTotalPedido) { this.valorTotalPedido = valorTotalPedido; }
    public List<Ingresso> getIngressosSelecionados() { return ingressosSelecionados; }
    public void setIngressosSelecionados(List<Ingresso> ingressosSelecionados) { this.ingressosSelecionados = ingressosSelecionados; }
    public List<ItemPedidoDTO> getItensResumo() { return itensResumo; }
    public void setItensResumo(List<ItemPedidoDTO> itensResumo) { this.itensResumo = itensResumo; }
    public MetodoPagamento getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) { this.metodoPagamento = metodoPagamento; }
}