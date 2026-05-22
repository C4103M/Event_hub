package org.hexanet.eventhub.dto;

import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.enums.MetodoPagamento;

import java.util.List;
public class ComprarIngressoDTO {
    private List<Ingresso> ingressos;
    private MetodoPagamento metodoPagamento;

    public ComprarIngressoDTO(List<Ingresso> ingressos, MetodoPagamento metodoPagamento) {
        this.ingressos = ingressos;
        this.metodoPagamento = metodoPagamento;
    }

    public List<Ingresso> getIngressos() {
        return ingressos;
    }

    public void setIngressos(List<Ingresso> ingressos) {
        this.ingressos = ingressos;
    }

    public MetodoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
}
