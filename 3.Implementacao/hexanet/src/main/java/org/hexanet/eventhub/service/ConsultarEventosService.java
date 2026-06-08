package org.hexanet.eventhub.service;

import org.hexanet.eventhub.dao.EventoDAO;
import org.hexanet.eventhub.dto.DetalhesEventoDTO;
import org.hexanet.eventhub.dto.TipoIngressoDTO;
import org.hexanet.eventhub.model.Evento;
import org.hexanet.eventhub.model.TipoIngresso;

import java.util.ArrayList;
import java.util.List;

public class ConsultarEventosService {
    EventoDAO eventoDAO = new EventoDAO();

    public ConsultarEventosService() {

    }

    public List<DetalhesEventoDTO> listarDetalhesPublicos() {

        List<Evento> eventos = this.eventoDAO.listarEventosPublicos();

        List<DetalhesEventoDTO> listaDetalhes = new ArrayList<>();

        for (Evento evento : eventos) {
            DetalhesEventoDTO dto = new DetalhesEventoDTO();
            dto.setEvento(evento);
            dto.setIdEvento(evento.getId());
            dto.setNome(evento.getNome());
            dto.setLocal(evento.getLocal());
            dto.setDataHora(evento.getDataHora());
            dto.setUrlImg(evento.getEventoImg());

            List<TipoIngressoDTO> tiposDTO = new ArrayList<>();

            if (evento.getTiposIngresso() != null) {
                for (TipoIngresso tipo : evento.getTiposIngresso()) {
                    TipoIngressoDTO tipoDTO = new TipoIngressoDTO();
                    tipoDTO.setId(tipo.getId());
                    tipoDTO.setNome(tipo.getNome());
                    tipoDTO.setPreco(tipo.getPreco());
                    tipoDTO.setQtdDisponiveis(tipo.getQtdDisponiveis());

                    tiposDTO.add(tipoDTO);
                }
            }

            dto.setTiposDisponiveis(tiposDTO);
            listaDetalhes.add(dto);
        }

        return listaDetalhes;
    }
}
