package org.hexanet.eventhub.dao.interfaces;

import jakarta.persistence.EntityManager;
import org.hexanet.eventhub.factory.EmFactory;
import java.util.List;

public abstract class BaseCrud<T, ID> implements CrudInterface<T, ID>{
    private final Class<T> classeEntidade; //Esse é uma classe, e não um objeto
    // Recebe a classe ao qual o herdeiro representa. Exemplo: Caso o herdeiro seja EventoDAO, o BaseCrudAbstract recebe o Evento
    // Para instanciar, deve se passar a classe e não uma instância de um objeto (utiliza o .class, exemplo: Evento.class)
    public BaseCrud(Class<T> classeEntidade) {
        this.classeEntidade = classeEntidade;
    }

    @Override
    public void salvar(T entidade) {
        EntityManager em = EmFactory.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entidade);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    @Override
    public T buscarPorId(ID id) {
        EntityManager em = EmFactory.getEntityManager();
        try {
            return em.find(classeEntidade, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> listarTodos() {
        EntityManager em = EmFactory.getEntityManager();
        try {
            // Java Persistence querry Language (O sql)
            String jpql = "SELECT e FROM " + classeEntidade.getSimpleName() + " e";
            return em.createQuery(jpql, classeEntidade).getResultList();
        } finally {
            em.close();
        }
    }
    @Override
    public void atualizar(T entidade) {
        EntityManager em = EmFactory.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entidade);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deletar(ID id) {
        EntityManager em = EmFactory.getEntityManager();
        try {
            em.getTransaction().begin();
            T entidade = em.find(classeEntidade, id);
            if (entidade != null) {
                em.remove(entidade);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
