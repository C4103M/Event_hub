package org.hexanet.eventhub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ingresso")
public class Ingresso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_ingresso_id", nullable = false)
    private TipoIngresso tipo;

    private String codigo;

    @ManyToOne
    @JoinColumn(name = "pedido_id") // Nome da coluna FK no banco
    private Pedido pedido;


    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


}
