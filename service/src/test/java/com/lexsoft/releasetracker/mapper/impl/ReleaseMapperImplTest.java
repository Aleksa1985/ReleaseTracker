package com.lexsoft.releasetracker.mapper.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.lexsoft.releasetracker.dto.ReleaseDto;
import com.lexsoft.releasetracker.dto.ReleaseWrapper;
import com.lexsoft.releasetracker.mapper.ReleaseMapper;
import com.lexsoft.releasetracker.repository.cache.ReleaseStatusCache;
import com.lexsoft.releasetracker.repository.model.ReleaseEntity;
import com.lexsoft.releasetracker.repository.model.ReleaseStatusEntity;
import com.lexsoft.releasetracker.utils.DateUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class ReleaseMapperImplTest {

    private ReleaseMapper releaseMapper;

    @Mock
    private ReleaseStatusCache releaseStatusCache;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        releaseMapper = new ReleaseMapperImpl(releaseStatusCache);
    }

    @Test
    public void testConvertToDto() {
        ReleaseEntity releaseEntity = createTestReleaseEntity();
        when(releaseStatusCache.fromCache(anyString())).thenReturn(releaseEntity.getReleaseStatus());

        ReleaseDto releaseDto = releaseMapper.convertToDto(releaseEntity);

        assertEquals(releaseEntity.getUuid().toString(), releaseDto.getId());
        assertEquals(DateUtils.dateToString(releaseEntity.getCreated()), releaseDto.getCreatedAt());
        assertEquals(DateUtils.dateToString(releaseEntity.getUpdated()), releaseDto.getLastUpdateAt());
        assertEquals(releaseEntity.getName(), releaseDto.getName());
        assertEquals(releaseEntity.getDescription(), releaseDto.getDescription());
        assertEquals(DateUtils.dateToString(releaseEntity.getReleaseDate()), releaseDto.getReleaseDate());
        assertEquals(releaseEntity.getReleaseStatus().getName(), releaseDto.getStatus());
    }

    @Test
    public void testConvertToEntity() {
        ReleaseDto releaseDto = createTestReleaseDto();
        when(releaseStatusCache.fromCache(anyString())).thenReturn(createTestReleaseEntity().getReleaseStatus());

        ReleaseEntity releaseEntity = releaseMapper.convertToEntity(releaseDto);

        assertEquals(releaseDto.getName(), releaseEntity.getName());
        assertEquals(releaseDto.getDescription(), releaseEntity.getDescription());
        assertEquals(DateUtils.stringToDate(releaseDto.getReleaseDate()), releaseEntity.getReleaseDate());
        assertEquals(releaseStatusCache.fromCache(releaseDto.getStatus()), releaseEntity.getReleaseStatus());
    }

    @Test
    public void testConvertToDtos() {
        List<ReleaseEntity> releaseEntities = Arrays.asList(createTestReleaseEntity());
        Page<ReleaseEntity> page = new PageImpl<>(releaseEntities, Pageable.unpaged(), releaseEntities.size());

        ReleaseWrapper releaseWrapper = releaseMapper.convertToDtos(page);

        assertEquals(1, releaseWrapper.getReleases().size());
        assertEquals(releaseEntities.get(0).getUuid().toString(), releaseWrapper.getReleases().get(0).getId());
        assertEquals(1, releaseWrapper.getTotalPages());
        assertEquals(1, releaseWrapper.getTotalRecords());
    }

    private ReleaseEntity createTestReleaseEntity() {
        return ReleaseEntity.builder()
                .uuid(UUID.randomUUID())
                .name("SOme")
                .description("desc")
                .releaseStatus(ReleaseStatusEntity.builder()
                        .order(0)
                        .name("status 1")
                        .build())
                .releaseDate(DateUtils.stringToDate("12/12/2024"))
                .updated(DateUtils.stringToDate("12/11/2024"))
                .created(DateUtils.stringToDate("13/11/2024"))
                .build();
    }

    private ReleaseDto createTestReleaseDto() {
        return ReleaseDto.builder()
                .status("Created")
                .releaseDate("12/12/2024")
                .description("description")
                .name("name")
                .lastUpdateAt("11/12/2024")
                .createdAt("10/12/2024")
                .id(UUID.randomUUID().toString())
                .build();
    }
}