package org.hexanet.eventhub.dao;

import jakarta.persistence.EntityManager;
import org.hexanet.eventhub.dao.interfaces.BaseCrud;
import org.hexanet.eventhub.dao.interfaces.CrudInterface;
import org.hexanet.eventhub.factory.EmFactory;
import org.hexanet.eventhub.model.Evento;

import java.util.List;

public class EventoDAO extends BaseCrud<Evento, Long> {

    public EventoDAO() {
        super(Evento.class);
    }
}
