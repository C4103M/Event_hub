package org.hexanet.eventhub.dao;

import org.hexanet.eventhub.dao.interfaces.BaseCrud;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.Participante;

public class ParticipanteDAO extends BaseCrud<Participante, Long> {
    public ParticipanteDAO() {
        super(Participante.class);
    }

    public Participante buscarPorEmail(String email) {
        jakarta.persistence.EntityManager em = org.hexanet.eventhub.factory.EmFactory.getEntityManager();
        try {
            String jpql = "SELECT p FROM Participante p WHERE p.email = :email";
            return em.createQuery(jpql, Participante.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}
