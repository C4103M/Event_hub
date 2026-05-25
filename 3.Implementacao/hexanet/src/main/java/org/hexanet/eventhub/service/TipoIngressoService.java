package org.hexanet.eventhub.service;

import org.hexanet.eventhub.dto.TipoIngressoDTO;
import org.hexanet.eventhub.model.Evento;
import org.hexanet.eventhub.dao.EventoDAO;

import java.util.List;
import java.util.stream.Collectors;

public class TipoIngressoService {

    private final EventoDAO eventoDAO = new EventoDAO();

    public List<TipoIngressoDTO> buscarTiposIngressoPorEvento(Long eventoId) {
        // Busca o evento (que por cascata/lazy traz seus respectivos tipos de ingressos)
        Evento evento = eventoDAO.buscarPorId(eventoId);

        if (evento == null) {
            throw new RuntimeException("Evento não encontrado no sistema.");
        }

        // Conversão de Entidade (Model) para DTO efetuada no Service
        return evento.getTiposIngresso().stream()
                .map(tipo -> new TipoIngressoDTO(
                        tipo.getId(),
                        tipo.getNome(),
                        tipo.getPreco(),
                        tipo.getQtdDisponiveis()
                ))
                .collect(Collectors.toList());
    }
}