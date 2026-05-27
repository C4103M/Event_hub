package org.hexanet.eventhub;

import org.hexanet.eventhub.dao.EventoDAO;
import org.hexanet.eventhub.dto.UsuarioDTO;
import org.hexanet.eventhub.model.Evento;
import org.hexanet.eventhub.model.Organizador;
import org.hexanet.eventhub.model.TipoIngresso;
import org.hexanet.eventhub.service.AuthService;

import java.time.LocalDateTime;

public class TesteCrud {

    private static final EventoDAO eventoDAO = new EventoDAO();

    public static void main() {
        TesteCrud.popularEventosTeste();;
    }


    public static void popularEventosTeste() {
        try {
            System.out.println("[SEEDER] Iniciando inserção de dados de teste...");

            // ==========================================
            // EVENTO 1: Festa Junina Foxes
            // ==========================================
            Evento festaJunina = new Evento();
            festaJunina.setNome("Festa Junina Foxes");
            festaJunina.setLocal("Centro de Eventos, São Paulo, SP");
            festaJunina.setDataHora(LocalDateTime.of(2026, 6, 24, 18, 0));
            festaJunina.setDescricao("A maior festa junina universitária está de volta! Prepare-se para muita comida típica, brincadeiras e atrações.");
            festaJunina.setCapacidadeTotal(500);

            // Utilizando o método utilitário bidirecional para amarrar o cascade
            festaJunina.addTipoIngresso(new TipoIngresso("Entrada Normal (Segundo Lote)", 18.50, 150));
            festaJunina.addTipoIngresso(new TipoIngresso("Open Food (Segundo Lote)", 45.00, 80));
            festaJunina.addTipoIngresso(new TipoIngresso("Open Bar (Segundo Lote)", 90.00, 50));

            // ==========================================
            // EVENTO 2: Tech Symposium 2026
            // ==========================================
            Evento techSymposium = new Evento();
            techSymposium.setNome("Tech Symposium Hub");
            techSymposium.setLocal("Auditório Paulista, São Paulo, SP");
            techSymposium.setDataHora(LocalDateTime.of(2026, 8, 12, 9, 0));
            techSymposium.setDescricao("O maior simpósio de tecnologia e arquitetura de software da América Latina.");
            techSymposium.setCapacidadeTotal(500);

            techSymposium.addTipoIngresso(new TipoIngresso("Ingresso Estudante", 25.00, 100));
            techSymposium.addTipoIngresso(new TipoIngresso("Inscrição Profissional", 70.00, 200));
            techSymposium.addTipoIngresso(new TipoIngresso("Acesso VIP + Workshops", 150.00, 30));
            // ==========================================
            // SALVANDO VIA DAO (CASCATA ATIVA)
            // ==========================================
            // O método salvar do seu DAO executará internamente o em.persist() ou em.merge().
            // Graças ao CascadeType.ALL, os tipos de ingresso serão salvos juntos!
            eventoDAO.salvar(festaJunina);
            eventoDAO.salvar(techSymposium);

            System.out.println("[SEEDER] Sucesso: 2 Eventos e seus respectivos TipoIngresso foram persistidos via DAO!");

        } catch (Exception e) {
            System.err.println("[SEEDER] Erro crítico ao popular dados através do EventoDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void createOrganizador() {
        Organizador org = new Organizador("Caio", "caio@org", "123", "231313131231");
        AuthService service = new AuthService();
        service.cadastrar(new UsuarioDTO(org.getNome(), org.getEmail(),  org.getCnpj(),null, org.getSenhaHash(), org.getSenhaHash()));
    }
}