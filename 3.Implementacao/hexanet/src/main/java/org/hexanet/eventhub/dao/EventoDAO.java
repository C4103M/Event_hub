package org.hexanet.eventhub.dao;

import jakarta.persistence.EntityManager;
import org.hexanet.eventhub.dao.interfaces.BaseCrud;
import org.hexanet.eventhub.dao.interfaces.CrudInterface;
import org.hexanet.eventhub.factory.EmFactory;
import org.hexanet.eventhub.model.Evento;
import org.hexanet.eventhub.model.enums.StatusEvento;
import java.util.List;

public class EventoDAO extends BaseCrud<Evento, Long> {

    public EventoDAO() {
        super(Evento.class);
    }

    public List<Evento> listarPorOrganizador(Long organizadorId) {
        EntityManager em = EmFactory.getEntityManager();
        try {
            String jpql = "SELECT e FROM Evento e WHERE e.organizador.id = :organizadorId";
            return em.createQuery(jpql, Evento.class)
                    .setParameter("organizadorId", organizadorId)
                    .getResultList();
        } finally {
            em.close();
        }
    }


    public List<Evento> listarEventosPublicos() {
        EntityManager em = EmFactory.getEntityManager();
        try {
            String jpql = "SELECT e FROM Evento e WHERE e.statusEvento NOT IN (:statusExcluidos)";
            return em.createQuery(jpql,Evento.class).setParameter("statusExcluidos",java.util.Arrays.asList(StatusEvento.CANCELADO,StatusEvento.EM_ANDAMENTO)).getResultList();
        }finally {
            em.close();
        }
    }
}

