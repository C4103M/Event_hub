package org.hexanet.eventhub.service;

import org.hexanet.eventhub.dao.EventoDAO;
import org.hexanet.eventhub.exceptions.AlterarEvento;
import org.hexanet.eventhub.exceptions.CapacidadeTotal;
import org.hexanet.eventhub.exceptions.EventoCancelado;
import org.hexanet.eventhub.exceptions.EventoNaoEncontrado;
import org.hexanet.eventhub.model.Evento;

import org.hexanet.eventhub.model.enums.StatusEvento;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class EventoService {
    private final EventoDAO eventoDAO;

    public EventoService() {
        this.eventoDAO = new EventoDAO();
    }

    public void cadastrarEvento(Evento evento, File imagem) throws IOException {
        evento.setQtdDisponiveis(evento.getCapacidadeTotal());

        if (evento.getStatusEvento() == null) {
            evento.setStatusEvento(StatusEvento.ABERTO);
        }

        if (imagem != null && imagem.exists()) {
            String assetsDir = "src/main/resources/org/hexanet/eventhub/assets";
            File dir = new File(assetsDir);
            if (!dir.exists())
                dir.mkdirs(); // cria o diretorio, se nao existir

            String imgName = UUID.randomUUID().toString() + "_" + imagem.getName();
            Path destiny = Paths.get(assetsDir, imgName);

            Files.copy(imagem.toPath(), destiny, StandardCopyOption.REPLACE_EXISTING);
            evento.setEventoImg("assets/images/" + imgName);
        }

        eventoDAO.salvar(evento);
    }

    public void atualizarEvento(Evento eventoAtualizado, File novaImagem) throws Exception {
        Evento existEvent = eventoDAO.buscarPorId(eventoAtualizado.getId());

        if (existEvent == null) {
            throw new EventoNaoEncontrado("Evento não encontrado no sistema");
        }

        StatusEvento status = existEvent.getStatusEvento();

        if (status == StatusEvento.CANCELADO || status == StatusEvento.FINALIZADO
                || status == StatusEvento.EM_ANDAMENTO) {
            throw new AlterarEvento("Não é possível alterar um evento que está " + status.name() + ".");
        }

        if (novaImagem != null && novaImagem.exists()) {
            String assetsDir = "src/main/resources/org/hexanet/eventhub/assets";
            File dir = new File(assetsDir);
            if (!dir.exists())
                dir.mkdirs();

            String imgName = UUID.randomUUID().toString() + "_" + novaImagem.getName();
            Path destiny = Paths.get(assetsDir, imgName);

            Files.copy(novaImagem.toPath(), destiny, StandardCopyOption.REPLACE_EXISTING);
            eventoAtualizado.setEventoImg("assets/images/" + imgName);
        } else {
            eventoAtualizado.setEventoImg(existEvent.getEventoImg());
        }

        int diferencaCapacidade = eventoAtualizado.getCapacidadeTotal() - existEvent.getCapacidadeTotal();
        int novaQtdDisponivel = existEvent.getQtdDisponiveis() + diferencaCapacidade;

        if (novaQtdDisponivel < 0) {
            throw new CapacidadeTotal(
                    "A nova capacidade do local é menor do que a quantidade de ingressos que já foram vendidos.");
        }

        eventoAtualizado.setQtdDisponiveis(novaQtdDisponivel);

        if (eventoAtualizado.getQtdDisponiveis() == 0 && eventoAtualizado.getStatusEvento() == StatusEvento.ABERTO) {
            eventoAtualizado.setStatusEvento(StatusEvento.ESGOTADO);
        } else if (eventoAtualizado.getQtdDisponiveis() > 0
                && eventoAtualizado.getStatusEvento() == StatusEvento.ESGOTADO) {
            eventoAtualizado.setStatusEvento(StatusEvento.ABERTO);
        }
        eventoDAO.atualizar(eventoAtualizado);

    }

    public void excluirEvento(long idEvento){
        Evento evento = eventoDAO.buscarPorId(idEvento);

        if (evento == null){
            throw new EventoNaoEncontrado("Evento não encontrado.");
        }

            StatusEvento status = evento.getStatusEvento();

            if (status == StatusEvento.RASCUNHO){
                eventoDAO.deletar(idEvento);
            }else{
                if (status == StatusEvento.FINALIZADO || status == StatusEvento.CANCELADO){
                    throw new EventoCancelado("O evento já está " + status.name() + ".");
                }

                evento.setStatusEvento(StatusEvento.CANCELADO);

                eventoDAO.atualizar(evento);
            }
    }
}
