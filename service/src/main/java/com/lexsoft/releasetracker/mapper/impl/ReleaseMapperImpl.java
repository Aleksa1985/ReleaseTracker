package com.lexsoft.releasetracker.mapper.impl;

import com.lexsoft.releasetracker.dto.ReleaseDto;
import com.lexsoft.releasetracker.dto.ReleaseWrapper;
import com.lexsoft.releasetracker.mapper.ReleaseMapper;
import com.lexsoft.releasetracker.repository.model.ReleaseEntity;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReleaseMapperImpl implements ReleaseMapper {

    @Override
    public ReleaseDto convertToDto(ReleaseEntity releaseEntity) {
        return null;
    }

    @Override
    public ReleaseEntity convertToEntity(ReleaseDto releaseDto) {
        return null;
    }

    @Override
    public List<ReleaseEntity> convertToEntityList(ReleaseWrapper releaseWrapper) {
        return null;
    }

    @Override
    public ReleaseWrapper convertToDtos(Page<ReleaseEntity> page) {
        return null;
    }
}
