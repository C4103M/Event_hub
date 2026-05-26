package org.hexanet.eventhub.model.enums;


public enum MetodoPagamento {
    PIX("PIX"),
    CREDITO("CREDITO"),
    DEBITO("DEBITO"),
    BOLETO("BOLETO");

    private final String descricao;

    MetodoPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static MetodoPagamento fromString(String texto) {
        for (MetodoPagamento m : MetodoPagamento.values()) {
            if (m.getDescricao().equalsIgnoreCase(texto)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Método de pagamento inválido: " + texto);
    }
}
