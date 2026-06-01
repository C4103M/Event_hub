package org.hexanet.eventhub.service;

import org.hexanet.eventhub.dao.EventoDAO;
import org.hexanet.eventhub.dto.DetalhesEventoDTO;
import org.hexanet.eventhub.dto.TipoIngressoDTO;
import org.hexanet.eventhub.exceptions.AlterarEvento;
import org.hexanet.eventhub.exceptions.CapacidadeTotal;
import org.hexanet.eventhub.exceptions.EventoCancelado;
import org.hexanet.eventhub.exceptions.EventoNaoEncontrado;
import org.hexanet.eventhub.model.Evento;

import org.hexanet.eventhub.model.TipoIngresso;
import org.hexanet.eventhub.model.enums.StatusEvento;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

public class ManterEventoService {
    private final EventoDAO eventoDAO;

    public ManterEventoService() {
        this.eventoDAO = new EventoDAO();
    }

    private Path obterDiretorioUpload() throws IOException{
        String userHome = System.getProperty("user.home");
        Path path = Paths.get(userHome,".eventhub","images");
        if (!Files.exists(path)){
            Files.createDirectories(path);
        }
        return path;
    }

    public void cadastrarEvento(Evento evento, File imagem) throws IOException {
        evento.setQtdDisponiveis(evento.getCapacidadeTotal());

        if (evento.getStatusEvento() == null) {
            evento.setStatusEvento(StatusEvento.ABERTO);
        }

        if (org.hexanet.eventhub.singleton.SessaoUsuario.getInstancia().isLogado() &&
            org.hexanet.eventhub.singleton.SessaoUsuario.getInstancia().getUsuarioLogado() instanceof org.hexanet.eventhub.model.Organizador) {
            evento.setOrganizador((org.hexanet.eventhub.model.Organizador) org.hexanet.eventhub.singleton.SessaoUsuario.getInstancia().getUsuarioLogado());
        }

        if (imagem != null && imagem.exists()) {
            String assetsDir = "src/main/resources/assets";
            File dir = new File(assetsDir);
            if (!dir.exists())
                dir.mkdirs(); // cria o diretorio, se nao existir

            String imgName = UUID.randomUUID().toString() + "_" + imagem.getName();
            Path targetPath = obterDiretorioUpload().resolve(imgName);

            Files.copy(imagem.toPath(),targetPath,StandardCopyOption.REPLACE_EXISTING);

            evento.setEventoImg(targetPath.toUri().toString());
        }
        eventoDAO.salvar(evento);
    }

    public void atualizarEvento(Evento eventoAtualizado, File novaImagem) throws Exception {
        jakarta.persistence.EntityManager em = org.hexanet.eventhub.factory.EmFactory.getEntityManager();
        try {
            em.getTransaction().begin();
            Evento existEvent = em.find(Evento.class, eventoAtualizado.getId());

            if (existEvent == null) {
                throw new EventoNaoEncontrado("Evento não encontrado no sistema");
            }

            StatusEvento status = existEvent.getStatusEvento();

            if (status == StatusEvento.CANCELADO || status == StatusEvento.FINALIZADO
                    || status == StatusEvento.EM_ANDAMENTO) {
                throw new AlterarEvento("Não é possível alterar um evento que está " + status.name() + ".");
            }

            String imagemCaminho = existEvent.getEventoImg();
            if (novaImagem != null && novaImagem.exists()) {
                if (imagemCaminho != null && imagemCaminho.startsWith("file:")) {
                    try {

                        java.nio.file.Path antigoCaminho = java.nio.file.Paths.get(java.net.URI.create(imagemCaminho));
                        java.nio.file.Files.deleteIfExists(antigoCaminho);
                    } catch (Exception e) {
                        System.err.println("Aviso: Não foi possível deletar o arquivo de imagem antigo: " + e.
                        getMessage());
                    }
                }

                String imgName = UUID.randomUUID().toString() + "_" + novaImagem.getName();
                Path targetPath = obterDiretorioUpload().resolve(imgName);
                Files.copy(novaImagem.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                imagemCaminho = targetPath.toUri().toString();
            }

            int diferencaCapacidade = eventoAtualizado.getCapacidadeTotal() - existEvent.getCapacidadeTotal();
            int novaQtdDisponivel = existEvent.getQtdDisponiveis() + diferencaCapacidade;

            if (novaQtdDisponivel < 0) {
                throw new CapacidadeTotal(
                        "A nova capacidade do local é menor do que a quantidade de ingressos que já foram vendidos.");
            }

            existEvent.setNome(eventoAtualizado.getNome());
            existEvent.setLocal(eventoAtualizado.getLocal());
            existEvent.setDescricao(eventoAtualizado.getDescricao());
            existEvent.setCapacidadeTotal(eventoAtualizado.getCapacidadeTotal());
            existEvent.setDataHora(eventoAtualizado.getDataHora());
            existEvent.setEventoImg(imagemCaminho);
            existEvent.setQtdDisponiveis(novaQtdDisponivel);

            if (existEvent.getOrganizador() == null && org.hexanet.eventhub.singleton.SessaoUsuario.getInstancia().isLogado() && 
                org.hexanet.eventhub.singleton.SessaoUsuario.getInstancia().getUsuarioLogado() instanceof org.hexanet.eventhub.model.Organizador) {
                existEvent.setOrganizador((org.hexanet.eventhub.model.Organizador) org.hexanet.eventhub.singleton.SessaoUsuario.getInstancia().getUsuarioLogado());
            }

            StatusEvento novoStatus = eventoAtualizado.getStatusEvento();
            if (novaQtdDisponivel == 0 && novoStatus == StatusEvento.ABERTO) {
                existEvent.setStatusEvento(StatusEvento.ESGOTADO);
            } else if (novaQtdDisponivel > 0 && novoStatus == StatusEvento.ESGOTADO) {
                existEvent.setStatusEvento(StatusEvento.ABERTO);
            } else {
                existEvent.setStatusEvento(novoStatus);
            }

            // Sincronizar a coleção tiposIngresso
            java.util.List<org.hexanet.eventhub.model.TipoIngresso> newTipos = eventoAtualizado.getTiposIngresso();
            java.util.List<org.hexanet.eventhub.model.TipoIngresso> currentTipos = existEvent.getTiposIngresso();

            // 1. Remover ingressos órfãos
            currentTipos.removeIf(current -> {
                boolean keep = newTipos.stream().anyMatch(n -> n.getNome().equalsIgnoreCase(current.getNome()));
                if (!keep) {
                    current.setEvento(null);
                }
                return !keep;
            });

            // 2. Adicionar ou atualizar ingressos
            for (org.hexanet.eventhub.model.TipoIngresso newTipo : newTipos) {
                org.hexanet.eventhub.model.TipoIngresso existing = currentTipos.stream()
                        .filter(c -> c.getNome().equalsIgnoreCase(newTipo.getNome()))
                        .findFirst()
                        .orElse(null);

                if (existing != null) {
                    existing.setPreco(newTipo.getPreco());
                    existing.setQtdDisponiveis(newTipo.getQtdDisponiveis());
                } else {
                    newTipo.setEvento(existEvent);
                    currentTipos.add(newTipo);
                }
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void excluirEvento(long idEvento){
        jakarta.persistence.EntityManager em = org.hexanet.eventhub.factory.EmFactory.getEntityManager();
        try {
            em.getTransaction().begin();
            Evento evento = em.find(Evento.class, idEvento);

            if (evento == null){
                throw new EventoNaoEncontrado("Evento não encontrado.");
            }

            StatusEvento status = evento.getStatusEvento();

            if (status == StatusEvento.RASCUNHO){
                em.remove(evento);
            }else{
                if (status == StatusEvento.FINALIZADO || status == StatusEvento.CANCELADO){
                    throw new EventoCancelado("O evento já está " + status.name() + ".");
                }

                evento.setStatusEvento(StatusEvento.CANCELADO);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Evento> listarTodos() {
        return eventoDAO.listarTodos();
    }

    public List<DetalhesEventoDTO> listarDetalhes() {
//        System.out.println("========================DEBUG listar detalhes ====================");
        List<Evento> eventos = this.listarTodos();

        // Lista que vai guardar o resultado final
        List<DetalhesEventoDTO> listaDetalhes = new ArrayList<>();

        // Laço passando por cada evento retornado do banco
        for (Evento evento : eventos) {
            DetalhesEventoDTO dto = new DetalhesEventoDTO();
            dto.setEvento(evento);
            // Mapeando os campos básicos
            dto.setIdEvento(evento.getId());
            dto.setNome(evento.getNome());
            dto.setLocal(evento.getLocal());
            dto.setDataHora(evento.getDataHora());
            dto.setUrlImg(evento.getEventoImg());

//            System.out.println("========================DEBUG listar detalhes ====================");
//            System.out.println(evento.getNome());

            List<TipoIngressoDTO> tiposDTO = new ArrayList<>();

            // Verifica se a lista não é nula antes de tentar percorrê-la
            if (evento.getTiposIngresso() != null) {

                // Laço passando por cada tipo de ingresso daquele evento específico
                for (TipoIngresso tipo : evento.getTiposIngresso()) {
                    TipoIngressoDTO tipoDTO = new TipoIngressoDTO();
                    tipoDTO.setId(tipo.getId());
                    tipoDTO.setNome(tipo.getNome());
                    tipoDTO.setPreco(tipo.getPreco());
                    tipoDTO.setQtdDisponiveis(tipo.getQtdDisponiveis());

                    tiposDTO.add(tipoDTO);
                }
            }

            // Atribui a lista de ingressos (vazia ou preenchida) ao DTO do evento
            dto.setTiposDisponiveis(tiposDTO);

            // Adiciona o evento totalmente montado na lista final
            listaDetalhes.add(dto);
        }

        return listaDetalhes;
    }


}
