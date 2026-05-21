package org.hexanet.eventhub.dao;

import org.hexanet.eventhub.dao.interfaces.BaseCrud;
import org.hexanet.eventhub.model.Ingresso;
import org.hexanet.eventhub.model.Participante;

public class ParticipanteDAO extends BaseCrud<Participante, Long> {
    public ParticipanteDAO() {
        super(Participante.class);
    }


}
