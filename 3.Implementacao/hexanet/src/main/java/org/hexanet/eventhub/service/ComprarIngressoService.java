package org.hexanet.eventhub.service;

import org.hexanet.eventhub.dao.IngressoDAO;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.Pedido;
import org.hexanet.eventhub.model.enums.StatusEvento;

import java.util.ArrayList;
import java.util.List;

public class ComprarIngressoService {
    private final IngressoDAO ingressoDAO = new IngressoDAO();

    public void comprarIngresso(Pedido pedido) {
        List<Ingresso> ingressosDisponiveis = new ArrayList<>();
        for(Ingresso ingresso : pedido.getIngressos()) {
            if(isDisponivel(ingresso)) {
                ingressosDisponiveis.add(ingresso);
                
            }
        }
    }

    private boolean isDisponivel(Ingresso ingresso) {
        return ingresso.getEvento().getStatusEvento() == StatusEvento.ABERTO;
    }
}
