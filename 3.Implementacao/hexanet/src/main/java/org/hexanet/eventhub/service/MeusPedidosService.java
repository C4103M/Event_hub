package org.hexanet.eventhub.service;

import org.hexanet.eventhub.dao.PedidoDAO;
import org.hexanet.eventhub.dto.DetalhesEventoDTO;
import org.hexanet.eventhub.dto.HistoricoPedidoDTO;
import org.hexanet.eventhub.dto.ItemPedidoDTO;
import org.hexanet.eventhub.model.Evento;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.Pedido;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeusPedidosService {
    private final PedidoDAO pedidoDAO = new PedidoDAO();

    public MeusPedidosService() {

    }


    public List<HistoricoPedidoDTO> listarMeusPedidos(Long idParticipante) {
        List<Pedido> meusPedidos = pedidoDAO.buscarMeusPedidos(idParticipante);
        List<HistoricoPedidoDTO> listaPedidos = new ArrayList<>();
        System.out.println("Ta chegando aqui");
        for(Pedido pedido: meusPedidos) {
            System.out.println("Não ta chegando aqui");
            if (pedido.getIngressos() == null || pedido.getIngressos().isEmpty()) {
                continue;
            }
            List<ItemPedidoDTO> itensComprados = agruparIngressos(pedido.getIngressos());
            HistoricoPedidoDTO historicoPedidoDTO = new HistoricoPedidoDTO(
                    pedido.getId(),
                    pedido.getPagamento().getValor(),
                    toDTO(pedido.getIngressos().get(0).getEvento()),
                    pedido.getIngressos().get(0).getEvento().getStatusEvento(),
                    itensComprados,
                    pedido.getIngressos()
            );
            System.out.println("Pedido: " + pedido.getId());
            listaPedidos.add(historicoPedidoDTO);
        }

        return listaPedidos;

    }

    public DetalhesEventoDTO toDTO(Evento evento) {

        return new DetalhesEventoDTO(
                evento.getId(),
                evento.getNome(),
                evento.getLocal(),
                evento.getDataHora(),
                evento.getEventoImg(),
                null
        );
    }
    private List<ItemPedidoDTO> agruparIngressos(List<Ingresso> ingressosFisicos) {
        Map<String, Integer> contagem = new HashMap<>();
        Map<String, Double> precos = new HashMap<>();

        for (Ingresso ingresso : ingressosFisicos) {
            // ATENÇÃO: Ajuste os getters abaixo de acordo com a sua classe Ingresso/TipoIngresso
            String nomeTipo = ingresso.getTipo().getNome();
            double precoUnitario = ingresso.getTipo().getPreco();

            // Soma +1 na quantidade deste tipo de ingresso
            contagem.put(nomeTipo, contagem.getOrDefault(nomeTipo, 0) + 1);

            // Guarda o preço unitário (ele só substitui se não existir)
            precos.putIfAbsent(nomeTipo, precoUnitario);
        }

        // Converte os mapas em uma lista de ItemPedidoDTO
        List<ItemPedidoDTO> itens = new ArrayList<>();
        for (String nomeTipo : contagem.keySet()) {
            itens.add(new ItemPedidoDTO(
                    nomeTipo,
                    precos.get(nomeTipo),
                    contagem.get(nomeTipo)
            ));
        }

        return itens;
    }
}
