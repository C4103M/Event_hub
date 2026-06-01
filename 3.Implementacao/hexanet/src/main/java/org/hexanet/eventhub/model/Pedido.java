package org.hexanet.eventhub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.w3c.dom.events.Event;

@Entity
@Table(name = "pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participante_id", nullable = false)
    private Participante participante;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pagamento_id")
    private Pagamento pagamento;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Ingresso> ingressos;

    public Pedido() {

    }

    public Pedido(Long id, LocalDateTime dataHora, List<Ingresso> ingressos, Evento evento) {
        this.id = id;
        this.dataHora = dataHora;
        this.ingressos = ingressos;
        this.pagamento = new Pagamento();
    }
    public Pedido(Long id, LocalDateTime dataHora, Ingresso ingresso) {
        this.id = id;
        this.dataHora = dataHora;
        this.pagamento = new Pagamento();

        this.addIngresso(ingresso);
    }
    public Participante getParticipante() {
        return participante;
    }

    public void setParticipante(Participante participante) {
        this.participante = participante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public List<Ingresso> getIngressos() {
        return ingressos;
    }

    public void setIngressos(List<Ingresso> ingressos) {
        this.ingressos = ingressos;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public void addIngresso(Ingresso ingresso) {
        if (this.ingressos == null) {
            this.ingressos = new ArrayList<Ingresso>();
        }
        this.ingressos.add(ingresso);
    }

    public double calcValorPedido() {
        double valor = 0;
        for(Ingresso ingresso : this.ingressos) {
            valor += ingresso.getTipo().getPreco();
        }
        return valor;
    }

}
