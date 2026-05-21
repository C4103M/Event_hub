package org.hexanet.eventhub.manager;

import org.hexanet.eventhub.model.Evento;
import org.hexanet.eventhub.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class IngressarManager {
    public static void main() {
        Usuario user = new Usuario();
        Evento evento = new Evento();

        user.setId(1L);
        evento.setId(2L);

        String codigoGerado = IngressarManager.gerarIngresso(user, evento);
        System.out.printf(codigoGerado);
        if(IngressarManager.verificarIngresso(codigoGerado, evento)) {
            System.out.printf("O codigo pertence a este evento");
        } else  {
            System.out.printf("O codigo n pertence a este evento");
        }

    }

    // CHAVE SECRETA:
    private static final String CHAVE_SECRETA = "minha-chave-secreta-super-segura-123";


    public static String gerarIngresso(Usuario usuario, Evento evento) {
        if (usuario.getId() == null || evento.getId() == null) {
            throw new IllegalArgumentException("Usuário e Evento precisam ter IDs válidos (salvos no banco).");
        }

        try {
            // Cria o payload puro
            String payload = usuario.getId() + ":" + evento.getId();

            // Juntar o payload com a chave secreta e passar no BCrypt
            String textoProtegido = payload + CHAVE_SECRETA;
            String assinaturaBcrypt = BCrypt.hashpw(textoProtegido, BCrypt.gensalt());

            // converte para Base64 URL-Safe
            String tokenPuro = payload + "." + assinaturaBcrypt;
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(tokenPuro.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            throw new RuntimeException("Falha ao gerar o ingresso com BCrypt", e);
        }
    }

    /**
     * Verifica a autenticidade e a qual evento o ingresso pertence
     */
    public static boolean verificarIngresso(String tokenBase64, Evento eventoAtual) {
        if (eventoAtual.getId() == null) {
            return false;
        }

        try {
            // Decodifica o Base64
            String tokenDecodificado = new String(
                    Base64.getUrlDecoder().decode(tokenBase64),
                    StandardCharsets.UTF_8
            );

            // Separa o Payload da Assinatura
            // O limite 2 garante que pontos internos gerados pelo BCrypt não quebrem a divisão
            String[] partes = tokenDecodificado.split("\\.", 2);
            if (partes.length != 2) {
                return false;
            }

            String payload = partes[0];
            String hashBcryptRecebido = partes[1];

            // Valida a Criptografia usando o checkpw
            String textoProtegido = payload + CHAVE_SECRETA;
            if (!BCrypt.checkpw(textoProtegido, hashBcryptRecebido)) {
                return false;
            }

            //  Verifica as Regras de Negócio
            String[] dadosPayload = payload.split(":");
            Long idUsuarioPayload = Long.parseLong(dadosPayload[0]);
            Long idEventoPayload = Long.parseLong(dadosPayload[1]);

            if (!idEventoPayload.equals(eventoAtual.getId())) {
                return false;
            }

            return true;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}