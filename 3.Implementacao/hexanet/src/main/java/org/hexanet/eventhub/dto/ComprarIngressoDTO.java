package org.hexanet.eventhub.dto;

import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.enums.MetodoPagamento;

import java.util.List;
public class ComprarIngressoDTO {
    private List<Ingresso> ingressos;
    private MetodoPagamento metodoPagamento;
}
