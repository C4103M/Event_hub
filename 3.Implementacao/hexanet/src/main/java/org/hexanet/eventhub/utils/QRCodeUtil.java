package org.hexanet.eventhub.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import org.hexanet.eventhub.model.Ingresso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRCodeUtil {

    public static Image gerarQRCodeJavaFX(String texto, int largura, int altura) {
        try {
            // 1. Gera a matriz de bits do QR Code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, largura, altura);

            // 2. Escreve a matriz num fluxo de bytes (formato PNG) na memória
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            // 3. Converte os bytes numa Imagem nativa do JavaFX
            return new Image(new ByteArrayInputStream(pngData));

        } catch (WriterException | IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao gerar o QR Code: " + e.getMessage());
            return null;
        }
    }
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

            // Funciona mas deveria passar pelo service
//            IngressoDAO ingressoDAO = new IngressoDAO();
//            Ingresso ingresso = ingressoDAO.verificarIngresso(idIngresso, uuid);
//            if (ingresso == null) return false; //(QR Code falso)
//            if (!ingresso.getEvento().getId().equals(idEventoAtual)) return false; // (Ingresso de outro evento)
//            if (ingresso.isUsado()) return false; //(Alguém já entrou com este ingresso!)
//            ingresso.setUsado(true);
//            ingressoDAO.atualizar(ingresso);

            return true;

        } catch (Exception e) {
            // Se o formato estiver errado (não tem o hífen, ou não é número), é falso.
            return false;
        }
    }
}
