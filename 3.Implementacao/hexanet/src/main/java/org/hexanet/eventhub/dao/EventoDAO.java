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
    // Por implementar a Base do crud genérica, ele herda os métodos de crud simples,
    // ficando aqui apenas oq for específico da classe

    public Evento buscarPorEmail(String email) {
        EntityManager em = EmFactory.getEntityManager();
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.email = :email";
            return em.createQuery(jpql, Evento.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }




}
