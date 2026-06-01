package org.hexanet.eventhub.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "ingresso")
public class Ingresso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_ingresso_id", nullable = false)
    private TipoIngresso tipo;

    @Column(name = "codigo_seguranca", unique = true, nullable = false, updatable = false)
    private String codigoSeguranca;

    @Column(name = "usado", nullable = false)
    private boolean usado = false;

    @ManyToOne
    @JoinColumn(name = "pedido_id") // Nome da coluna FK no banco
    private Pedido pedido;


    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

    public Ingresso() {

    }

    @PrePersist
    public void gerarCodigoSeguranca() {
        if (this.codigoSeguranca == null) {
            this.codigoSeguranca = UUID.randomUUID().toString();
        }
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoIngresso getTipo() {
        return tipo;
    }

    public void setTipo(TipoIngresso tipo) {
        this.tipo = tipo;
    }

    public String getCodigoSeguranca() {
        return codigoSeguranca;
    }

    public void setCodigoSeguranca(String codigoSeguranca) {
        this.codigoSeguranca = codigoSeguranca;
    }

    public boolean isUsado() {
        return usado;
    }

    public void setUsado(boolean usado) {
        this.usado = usado;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
