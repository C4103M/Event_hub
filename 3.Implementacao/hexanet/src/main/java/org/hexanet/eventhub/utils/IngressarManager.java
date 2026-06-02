package org.hexanet.eventhub.utils;

import org.hexanet.eventhub.model.Evento;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class IngressarManager {

    // CHAVE SECRETA:
    private static final String CHAVE_SECRETA = "minha-chave-secreta-super-segura-123";


    public static String formatarDadosQrCode(Ingresso ingresso) {
        // Validação de segurança: garante que o objeto não é "fantasma"
        if (ingresso.getId() == null || ingresso.getCodigoSeguranca() == null) {
            throw new IllegalArgumentException("O ingresso precisa estar salvo no banco (ter ID e Código) antes de gerar o QR Code.");
        }

        // Retorna "1052-550e8400-e29b-41d4-a716-446655440000"
        return ingresso.getId() + "-" + ingresso.getCodigoSeguranca();
    }


    public static boolean verificarIngresso(String tokenLido, Long idEventoAtual) {
        try {
            String[] partes = tokenLido.split("-", 2);
            Long idIngresso = Long.parseLong(partes[0]);
            String uuid = partes[1];

            // AQUI ENTRA A LÓGICA DE BANCO DE DADOS (DAO/EntityManager):
            // 1. SELECT * FROM ingressos WHERE id = idIngresso AND hash_seguranca = uuid
            // 2. if (ingresso == null) return false; (QR Code falso)
            // 3. if (!ingresso.getEvento().getId().equals(idEventoAtual)) return false; (Ingresso de outro evento)
            // 4. if (ingresso.isUsado()) return false; (Alguém já entrou com este ingresso!)

            // 5. ingresso.setUsado(true);
            // 6. ingressoDao.atualizar(ingresso);

            return true; // Sucesso, pode libertar a catraca!

        } catch (Exception e) {
            // Se o formato estiver errado (não tem o hífen, ou não é número), é falso.
            return false;
        }
    }
}