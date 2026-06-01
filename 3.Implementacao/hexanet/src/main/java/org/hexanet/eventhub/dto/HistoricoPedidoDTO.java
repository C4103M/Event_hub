package org.hexanet.eventhub.dto;

import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.enums.StatusEvento;

import java.util.List;

public class HistoricoPedidoDTO {
    private Long idPedido;
    private double valorTotalPago;
    private DetalhesEventoDTO detalhesEvento;

    private StatusEvento statusEvento;
    private List<ItemPedidoDTO> itensComprados;
    private List<Ingresso> listaIngressos;

    public HistoricoPedidoDTO(Long idPedido, double valorTotalPago, DetalhesEventoDTO detalhesEvento, StatusEvento statusEvento, List<ItemPedidoDTO> itensComprados, List<Ingresso> listaIngressos) {
        this.idPedido = idPedido;
        this.valorTotalPago = valorTotalPago;
        this.detalhesEvento = detalhesEvento;
        this.statusEvento = statusEvento;
        this.itensComprados = itensComprados;
        this.listaIngressos = listaIngressos;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public double getValorTotalPago() {
        return valorTotalPago;
    }

    public void setValorTotalPago(double valorTotalPago) {
        this.valorTotalPago = valorTotalPago;
    }

    public DetalhesEventoDTO getDetalhesEvento() {
        return detalhesEvento;
    }

    public void setEvento(DetalhesEventoDTO detalhesEvento) {
        this.detalhesEvento = detalhesEvento;
    }

    public StatusEvento getStatusEvento() {
        return statusEvento;
    }

    public void setStatusEvento(StatusEvento statusEvento) {
        this.statusEvento = statusEvento;
    }

    public List<ItemPedidoDTO> getItensComprados() {
        return itensComprados;
    }

    public void setItensComprados(List<ItemPedidoDTO> itensComprados) {
        this.itensComprados = itensComprados;
    }

    public void setDetalhesEvento(DetalhesEventoDTO detalhesEvento) {
        this.detalhesEvento = detalhesEvento;
    }

    public List<Ingresso> getListaIngressos() {
        return listaIngressos;
    }

    public void setListaIngressos(List<Ingresso> listaIngressos) {
        this.listaIngressos = listaIngressos;
    }
}
