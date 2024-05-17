package com.martincastroalvarez.hex.hex.adapters.mappers;

import com.martincastroalvarez.hex.hex.adapters.entities.WorkEntity;
import com.martincastroalvarez.hex.hex.adapters.dto.WorkDTO;
import com.martincastroalvarez.hex.hex.domain.models.Work;

public class WorkMapper {
    public static Work toWork(WorkEntity workEntity) {
        Work work = new Work();
        work.setId(workEntity.getId());
        work.setStartTime(workEntity.getStartTime());
        work.setEndTime(workEntity.getEndTime());
        work.setUser(UserMapper.toUser(workEntity.getUser()));
        return work;
    }

    public static WorkEntity toWorkEntity(Work work) {
        WorkEntity workEntity = new WorkEntity();
        workEntity.setId(work.getId());
        workEntity.setStartTime(work.getStartTime());
        workEntity.setEndTime(work.getEndTime());
        workEntity.setUser(UserMapper.toUserEntity(work.getUser()));
        return workEntity;
    }

    public static WorkDTO toWorkDTO(Work work) {
        WorkDTO workDTO = new WorkDTO();
        workDTO.setId(work.getId());
        workDTO.setStartTime(work.getStartTime());
        workDTO.setEndTime(work.getEndTime());
        workDTO.setUserId(work.getUser().getId());
        return workDTO;
    }
}
