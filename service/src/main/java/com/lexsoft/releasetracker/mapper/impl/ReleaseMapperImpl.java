package com.lexsoft.releasetracker.mapper.impl;

import com.lexsoft.releasetracker.dto.ReleaseDto;
import com.lexsoft.releasetracker.dto.ReleaseWrapper;
import com.lexsoft.releasetracker.mapper.ReleaseMapper;
import com.lexsoft.releasetracker.repository.cache.ReleaseStatusCache;
import com.lexsoft.releasetracker.repository.model.ReleaseEntity;
import com.lexsoft.releasetracker.utils.DateUtils;

import java.text.ParseException;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReleaseMapperImpl implements ReleaseMapper {

    private final ReleaseStatusCache releaseStatusCache;

    @Override
    public ReleaseDto convertToDto(ReleaseEntity releaseEntity) {
        return ReleaseDto.builder()
                .id(releaseEntity.getUuid().toString())
                .createdAt(DateUtils.dateToString(releaseEntity.getCreated()))
                .lastUpdateAt(DateUtils.dateToString(releaseEntity.getUpdated()))
                .name(releaseEntity.getName())
                .description(releaseEntity.getDescription())
                .releaseDate(DateUtils.dateToString(releaseEntity.getReleaseDate()))
                .status(releaseEntity.getReleaseStatus().getName())
                .build();
    }

    @Override
    public ReleaseEntity convertToEntity(ReleaseDto releaseDto) {
       return ReleaseEntity.builder()
               .description(releaseDto.getDescription())
               .releaseDate(DateUtils.stringToDate(releaseDto.getReleaseDate()))
               .name(releaseDto.getName())
               .releaseStatus(releaseStatusCache.fromCache(releaseDto.getStatus()))
               .build();
    }

    @Override
    public ReleaseWrapper convertToDtos(Page<ReleaseEntity> page) {
        return ReleaseWrapper.builder()
                .releases(page.getContent().stream().map(e -> convertToDto(e)).collect(Collectors.toList()))
                .totalRecords(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
