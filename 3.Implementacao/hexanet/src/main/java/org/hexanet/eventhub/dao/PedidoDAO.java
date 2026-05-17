package org.hexanet.eventhub.dao;

import org.hexanet.eventhub.dao.interfaces.BaseCrud;
import org.hexanet.eventhub.model.Pedido;

public class PedidoDAO extends BaseCrud<Pedido, Long> {
    public PedidoDAO() {
        super(Pedido.class);
    }
}
