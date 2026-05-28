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
//        System.out.println("==== INICIANDO BUSCA DE EMAIL ====");
//        System.out.println("Email recebido: '" + email + "'");
//        System.out.println("Tamanho da string: " + email.length());
        try {
            String jpql = "SELECT u FROM Usuario u WHERE u.email = :email";
            return em.createQuery(jpql, Usuario.class)
                    .setParameter("email", email)
                    .getResultStream()   // Transforma em Stream
                    .findFirst()         // Pega o primeiro se existir
                    .orElse(null);

        }
        finally {
            em.close();
        }
    }
}
