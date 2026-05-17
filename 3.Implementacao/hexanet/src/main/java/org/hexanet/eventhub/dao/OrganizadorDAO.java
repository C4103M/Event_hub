package org.hexanet.eventhub.dao;

import org.hexanet.eventhub.dao.interfaces.BaseCrud;
import org.hexanet.eventhub.model.Organizador;

public class OrganizadorDAO extends BaseCrud<Organizador, Long> {
    public OrganizadorDAO() {
        super(Organizador.class);
    }
}
