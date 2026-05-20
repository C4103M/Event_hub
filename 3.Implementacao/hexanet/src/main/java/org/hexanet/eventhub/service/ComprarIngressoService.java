package org.hexanet.eventhub.service;

import org.hexanet.eventhub.dao.IngressoDAO;
import org.hexanet.eventhub.dao.PagamentoDAO;
import org.hexanet.eventhub.dao.PedidoDAO;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.Pagamento;
import org.hexanet.eventhub.model.Pedido;
import org.hexanet.eventhub.model.enums.MetodoPagamento;
import org.hexanet.eventhub.model.enums.StatusEvento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComprarIngressoService {
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    public ComprarIngressoService() {}

    public void comprarIngresso(Pedido pedido) {
        List<Ingresso> ingressosDisponiveis = new ArrayList<>();
        int disponiveis = 100;
        for(Ingresso ingresso : pedido.getIngressos()) {
            if(isDisponivel(ingresso) ) {
                ingressosDisponiveis.add(ingresso);
            }
        }
        pedido.setIngressos(ingressosDisponiveis);
        pagar(pedido);
    }

    private boolean isDisponivel(Ingresso ingresso) {
        int qtd = ingresso.getEvento().getQtdDisponiveis();
        if(qtd > 0) {
            return ingresso.getEvento().getStatusEvento() == StatusEvento.ABERTO;
        }
        ingresso.getEvento().setQtdDisponiveis(qtd-1);
        ingresso.getEvento().setStatusEvento(StatusEvento.ESGOTADO);
        return false;
    }

    private void pagar(Pedido pedido) {
        Pagamento pg = new Pagamento();
        pg.setDataHora(LocalDateTime.now());
        pg.setValor(pedido.calcValorPedido());
        pg.setMetodo(MetodoPagamento.PIX);
        pedido.setPagamento(pg);

        pedidoDAO.salvar(pedido);
    }
}
