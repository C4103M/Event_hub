package org.hexanet.eventhub.dao.interfaces;
import java.util.List;
public interface CrudInterface<T, ID> {
    void salvar(T entidade);
    T buscarPorId(ID id);
    List<T> listarTodos();
    void atualizar(T entidade);
    void deletar(ID id);

}
