package com.lexsoft.releasetracker.mapper;

import com.lexsoft.releasetracker.dto.ReleaseDto;
import com.lexsoft.releasetracker.dto.ReleaseWrapper;
import com.lexsoft.releasetracker.repository.model.ReleaseEntity;

import java.util.List;

import org.springframework.data.domain.Page;

public interface ReleaseMapper {

    ReleaseDto convertToDto(ReleaseEntity releaseEntity);
    ReleaseEntity convertToEntity(ReleaseDto releaseDto);
    List<ReleaseEntity> convertToEntityList(ReleaseWrapper releaseWrapper);
    ReleaseWrapper convertToDtos(Page<ReleaseEntity> page);

}
