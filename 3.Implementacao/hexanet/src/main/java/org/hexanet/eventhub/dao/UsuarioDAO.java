package org.hexanet.eventhub.dao;

import org.hexanet.eventhub.dao.interfaces.BaseCrud;
import org.hexanet.eventhub.model.Participante;
import org.hexanet.eventhub.model.Usuario;
import jakarta.persistence.EntityManager;

public class UsuarioDAO extends BaseCrud<Usuario, Long> {
    public UsuarioDAO() {
        super(Usuario.class);
    }

    public Usuario buscarPorEmail(String email) {
        EntityManager em = org.hexanet.eventhub.factory.EmFactory.getEntityManager();
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.email = :email";
            return em.createQuery(jpql, Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();

        } finally {
            em.close();
        }
    }
}
