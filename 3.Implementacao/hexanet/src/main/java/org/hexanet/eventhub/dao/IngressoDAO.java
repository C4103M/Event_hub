package org.hexanet.eventhub.dao;

import org.hexanet.eventhub.dao.interfaces.BaseCrud;
import org.hexanet.eventhub.model.Ingresso;

public class IngressoDAO extends BaseCrud<Ingresso, Long> {
    public IngressoDAO() {
        super(Ingresso.class);
    }
}
