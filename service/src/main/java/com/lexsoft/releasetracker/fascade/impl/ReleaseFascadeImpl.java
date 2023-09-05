package com.lexsoft.releasetracker.fascade.impl;

import com.lexsoft.releasetracker.dto.ReleaseDto;
import com.lexsoft.releasetracker.dto.ReleaseWrapper;
import com.lexsoft.releasetracker.fascade.ReleaseFascade;
import com.lexsoft.releasetracker.mapper.ReleaseMapper;
import com.lexsoft.releasetracker.repository.model.ReleaseEntity;
import com.lexsoft.releasetracker.service.ReleaseService;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReleaseFascadeImpl implements ReleaseFascade {

    private final ReleaseService releaseService;
    private final ReleaseMapper releaseMapper;

    @Override
    public ReleaseDto updateRelease(UUID id, ReleaseDto releaseDto) {
        releaseService.getReleaseById(id);
        ReleaseEntity releaseEntity = releaseMapper.convertToEntity(releaseDto);
        return releaseMapper.convertToDto(releaseService.updateRelease(releaseEntity));
    }

    @Override
    public ReleaseDto saveRelease(ReleaseDto releaseDto) {
        ReleaseEntity releaseEntity = releaseMapper.convertToEntity(releaseDto);
        return releaseMapper.convertToDto(releaseService.saveRelease(releaseEntity));
    }

    @Override
    public ReleaseWrapper getReleases(String name, String description, String releaseDateFrom, String releaseDateTo, String status, Integer page, Integer size) {
        return releaseMapper.convertToDtos(
                releaseService.getReleases(name,
                        description,
                        releaseDateFrom,
                        releaseDateTo,
                        status,
                        page,
                        size));
    }

    @Override
    public ReleaseDto getRelease(UUID id) {
        return releaseMapper.convertToDto(releaseService.getReleaseById(id));
    }

    @Override
    public boolean deleteRelease(UUID id) {
        return releaseService.deleteRelease(id);
    }
}
