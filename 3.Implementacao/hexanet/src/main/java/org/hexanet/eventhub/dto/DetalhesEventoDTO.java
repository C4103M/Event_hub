package org.hexanet.eventhub.dto;

import org.hexanet.eventhub.dao.EventoDAO;
import org.hexanet.eventhub.model.Evento;

import java.time.LocalDateTime;
import java.util.List;

public class DetalhesEventoDTO {
    private Long idEvento;
    private String nome;
    private String local;
    private LocalDateTime dataHora;
    private String urlImg;
    private List<TipoIngressoDTO> tiposDisponiveis;
    private Evento evento;
    public DetalhesEventoDTO() {
    }

    public DetalhesEventoDTO(Long idEvento, String nome, String local, LocalDateTime dataHora, String urlImg, List<TipoIngressoDTO> tiposDisponiveis) {
        this.idEvento = idEvento;
        this.nome = nome;
        this.local = local;
        this.dataHora = dataHora;
        this.urlImg = urlImg;
        this.tiposDisponiveis = tiposDisponiveis;
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public List<TipoIngressoDTO> getTiposDisponiveis() {
        return tiposDisponiveis;
    }

    public void setTiposDisponiveis(List<TipoIngressoDTO> tiposDisponiveis) {
        this.tiposDisponiveis = tiposDisponiveis;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}
