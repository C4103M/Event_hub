package org.hexanet.eventhub.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;

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
}
