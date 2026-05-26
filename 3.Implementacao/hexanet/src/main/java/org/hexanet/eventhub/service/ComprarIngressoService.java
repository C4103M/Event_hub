package org.hexanet.eventhub.service;

import org.hexanet.eventhub.dao.EventoDAO;
import org.hexanet.eventhub.dao.IngressoDAO;
import org.hexanet.eventhub.dao.PagamentoDAO;
import org.hexanet.eventhub.dao.PedidoDAO;
import org.hexanet.eventhub.dto.ComprarIngressoDTO;
import org.hexanet.eventhub.exceptions.IngressoNaoDisponivelException;
import org.hexanet.eventhub.exceptions.PermissaoNegada;
import org.hexanet.eventhub.model.*;
import org.hexanet.eventhub.model.enums.MetodoPagamento;
import org.hexanet.eventhub.model.enums.StatusEvento;
import org.hexanet.eventhub.singleton.SessaoUsuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComprarIngressoService {
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final EventoDAO eventoDAO = new EventoDAO();

    public ComprarIngressoService() {}

    public void comprarIngresso(ComprarIngressoDTO comprarIngressoDTO) {
        if((SessaoUsuario.getInstancia().getUsuarioLogado() instanceof Organizador organizador)){
            throw new PermissaoNegada("Organizador não pode comprar ingresso");
        }
        if(SessaoUsuario.getInstancia().getUsuarioLogado() == null) {
            throw new PermissaoNegada("Deve fazer login para comprar ingresso");
        }

        Participante participante = (Participante) SessaoUsuario.getInstancia().getUsuarioLogado();

        Pedido pedido = new Pedido();

        pedido.setIngressos(comprarIngressoDTO.getIngressosSelecionados());
        pedido.setDataHora(LocalDateTime.now());
        pedido.setParticipante(participante);

        for(Ingresso ingresso : pedido.getIngressos()) {

            // Está vindo errado, tem que resolver
            if(isDisponivel(ingresso) ) {
                throw new IngressoNaoDisponivelException("Ingresso para o evento " + ingresso.getEvento().getNome() + " esgotado");
            }
            ingresso.getEvento().subtrairQtdDisponiveis(1);
            if (ingresso.getEvento().getQtdDisponiveis() == 0) {
                ingresso.getEvento().setStatusEvento(StatusEvento.ESGOTADO);
            }
        }
        pagar(pedido);
    }

    private boolean isDisponivel(Ingresso ingresso) {
        System.out.printf("%d\n%s", ingresso.getEvento().getQtdDisponiveis(), ingresso.getEvento().getStatusEvento());
        return ingresso.getEvento().getQtdDisponiveis() > 0
                && ingresso.getEvento().getStatusEvento() == StatusEvento.ABERTO;
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
