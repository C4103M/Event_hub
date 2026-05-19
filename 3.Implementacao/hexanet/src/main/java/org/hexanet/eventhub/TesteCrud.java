package org.hexanet.eventhub;

import org.hexanet.eventhub.dao.EventoDAO;
import org.hexanet.eventhub.dao.ParticipanteDAO;
import org.hexanet.eventhub.model.Evento;
import org.hexanet.eventhub.model.Participante;
import org.hexanet.eventhub.model.enums.StatusEvento;

import java.time.LocalDateTime;

public class TesteCrud {
    public static void main() {
//        Participante p = new Participante("Caio", "caio@emanoel", "16614605690", null, "1234");
//        pDao.atualizar(p);

//        ParticipanteDAO pDao = new ParticipanteDAO();
//        Participante p = pDao.buscarPorId(1L);
//        p.setNome("Chuchu");
//        pDao.atualizar(p);
        EventoDAO eviadao = new EventoDAO();
        Evento e = new Evento("Festa do chuchu", "Salto", 100, LocalDateTime.now(), StatusEvento.ABERTO);
        Evento e2 = new Evento("Festa do abacate", "Salto", 100, LocalDateTime.now(), StatusEvento.ABERTO);
        Evento e3 = new Evento("Festa do morango", "Salto", 100, LocalDateTime.now(), StatusEvento.ABERTO);

        eviadao.salvar(e);
        eviadao.salvar(e2);
        eviadao.salvar(e3);

    }
}
