package org.hexanet.eventhub.factory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


// EntityManager é o equivalente da fabrica de conexão visto em aula
public class EmFactory {
    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("ev-hub");

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }
}
