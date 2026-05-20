package org.hexanet.eventhub.dao;

import org.hexanet.eventhub.dao.interfaces.BaseCrud;
import org.hexanet.eventhub.model.Pagamento;
public class PagamentoDAO extends BaseCrud<Pagamento, Long> {
    public PagamentoDAO() {
        super(Pagamento.class);
    }
}
