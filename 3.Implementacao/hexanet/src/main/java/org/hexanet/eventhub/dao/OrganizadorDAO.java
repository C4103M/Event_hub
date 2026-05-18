package org.hexanet.eventhub.dao;

import org.hexanet.eventhub.dao.interfaces.BaseCrud;
import org.hexanet.eventhub.model.Organizador;

public class OrganizadorDAO extends BaseCrud<Organizador, Long> {
    public OrganizadorDAO() {
        super(Organizador.class);
    }

    public Organizador buscarPorEmail(String email) {
        jakarta.persistence.EntityManager em = org.hexanet.eventhub.factory.EmFactory.getEntityManager();
        try {
            String jpql = "SELECT o FROM Organizador o WHERE o.email = :email";
            return em.createQuery(jpql, Organizador.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }
}
