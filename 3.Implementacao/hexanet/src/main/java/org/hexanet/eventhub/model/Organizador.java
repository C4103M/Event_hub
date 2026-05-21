package org.hexanet.eventhub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "organizador")
public class Organizador extends Usuario {
    private String cnpj;

    @OneToMany(mappedBy = "organizador")
    private List<Evento> eventos = new ArrayList<>();

    public Organizador() { super();}

    public Organizador(String nome, String email, String senhaHash, String cnpj) {
        super(nome, email, senhaHash);
        this.cnpj = cnpj;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

}
