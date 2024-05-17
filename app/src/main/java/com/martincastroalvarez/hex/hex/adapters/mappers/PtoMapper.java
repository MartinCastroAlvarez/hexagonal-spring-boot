package com.martincastroalvarez.hex.hex.adapters.mappers;

import com.martincastroalvarez.hex.hex.adapters.entities.PtoEntity;
import com.martincastroalvarez.hex.hex.adapters.dto.PtoDTO;
import com.martincastroalvarez.hex.hex.domain.models.Pto;

public class PtoMapper {
    public static Pto toPto(PtoEntity ptoEntity) {
        Pto pto = new Pto();
        pto.setId(ptoEntity.getId());
        pto.setDay(ptoEntity.getDay());
        pto.setType(ptoEntity.getType());
        pto.setUser(UserMapper.toUser(ptoEntity.getUser()));
        return pto;
    }

    public static PtoEntity toPtoEntity(Pto pto) {
        PtoEntity ptoEntity = new PtoEntity();
        ptoEntity.setId(pto.getId());
        ptoEntity.setDay(pto.getDay());
        ptoEntity.setType(pto.getType());
        ptoEntity.setUser(UserMapper.toUserEntity(pto.getUser()));
        return ptoEntity;
    }

    public static PtoDTO toPtoDTO(Pto pto) {
        PtoDTO ptoDTO = new PtoDTO();
        ptoDTO.setId(pto.getId());
        ptoDTO.setDay(pto.getDay());
        ptoDTO.setType(pto.getType().name());
        ptoDTO.setUserId(pto.getUser().getId());
        return ptoDTO;
    }
}
