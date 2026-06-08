package org.hexanet.eventhub.dao;

import jakarta.persistence.EntityManager;
import org.hexanet.eventhub.dao.interfaces.BaseCrud;
import org.hexanet.eventhub.factory.EmFactory;
import org.hexanet.eventhub.model.Ingresso;

public class IngressoDAO extends BaseCrud<Ingresso, Long> {
    public IngressoDAO() {
        super(Ingresso.class);
    }

    public Ingresso verificarIngresso(Long idIngresso, String uuid) throws Exception {
        EntityManager em = EmFactory.getEntityManager();
        try {
            String jpql = "SELECT i FROM Ingresso i WHERE i.id = :id AND i.hashSeguranca = :uuid";
            Ingresso ingresso = em.createQuery(jpql, Ingresso.class)
                    .setParameter("id", idIngresso)
                    .setParameter("uuid", uuid)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            return ingresso;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            em.close();
        }
    }
}
