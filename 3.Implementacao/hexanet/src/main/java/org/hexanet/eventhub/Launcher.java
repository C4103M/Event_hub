package org.hexanet.eventhub;

import jakarta.persistence.Persistence;

public class Launcher {
    public static void main(String[] args) {
        try {
            Persistence.createEntityManagerFactory("ev-hub");
            System.out.println("Tabelas criadas/verificadas com sucesso");
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar com o banco de dados", e);
        }
        javafx.application.Application.launch(Application.class, args);

    }
}
