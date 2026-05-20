package org.hexanet.eventhub.model.enums;

public enum TipoIngresso {
    PREFERENCIAL(0.5),
    NORMAL(1.0),
    VIP(2.0);

    private final double fator;

    TipoIngresso(double fator) {
        this.fator = fator;
    }

    public double getFator() {
        return this.fator;
    }
}
