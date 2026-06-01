package org.hexanet.eventhub.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hexanet.eventhub.dao.interfaces.BaseCrud;
import org.hexanet.eventhub.factory.EmFactory;
import org.hexanet.eventhub.model.Pedido;
import java.util.List;

public class PedidoDAO extends BaseCrud<Pedido, Long> {
    public PedidoDAO() {
        super(Pedido.class);
    }

    public List<Pedido> buscarMeusPedidos(Long idParticipante) {
        EntityManager em = EmFactory.getEntityManager();
        try {
            String jpql = "SELECT p FROM Pedido p JOIN FETCH p.ingressos WHERE p.participante.id = :id";
            TypedQuery<Pedido> query = em.createQuery(jpql, Pedido.class);
            query.setParameter("id", idParticipante);
            return query.getResultList();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }
}
