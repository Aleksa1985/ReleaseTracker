package com.lexsoft.releasetracker.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.lexsoft.releasetracker.exception.BusinessException;
import com.lexsoft.releasetracker.repository.ReleaseRepository;
import com.lexsoft.releasetracker.repository.cache.ReleaseStatusCache;
import com.lexsoft.releasetracker.repository.model.ReleaseEntity;
import com.lexsoft.releasetracker.repository.model.ReleaseStatusEntity;
import com.lexsoft.releasetracker.service.ReleaseService;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

class ReleaseServiceImplTest {

    @Mock
    private ReleaseStatusCache releaseStatusCache;

    @Mock
    private ReleaseRepository releaseRepository;

    private ReleaseService releaseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        releaseService = new ReleaseServiceImpl(releaseStatusCache, releaseRepository);
    }

    @Test
    public void testSaveReleaseSuccess() {
        String entityName = "New Release";
        ReleaseStatusEntity status = ReleaseStatusEntity.builder()
                .name("status")
                .order(0).build();

        ReleaseEntity releaseEntityToSave = new ReleaseEntity();
        releaseEntityToSave.setName(entityName);
        releaseEntityToSave.setDescription("something");
        releaseEntityToSave.setActive(true);
        releaseEntityToSave.setReleaseStatus(status);

        when(releaseRepository.findByNameAndActive(entityName, true)).thenReturn(Optional.empty());
        when(releaseRepository.save(releaseEntityToSave)).thenReturn(releaseEntityToSave);
        when(releaseStatusCache.fromCache(releaseEntityToSave.getReleaseStatus().getName())).thenReturn(status);

        ReleaseEntity savedReleaseEntity = releaseService.saveRelease(releaseEntityToSave);
        assertNotNull(savedReleaseEntity);
    }

    @Test
    public void testSaveReleaseDuplicateName() {
        String entityName = "New Release";
        ReleaseEntity existingReleaseEntity = new ReleaseEntity();
        existingReleaseEntity.setName(entityName);
        existingReleaseEntity.setActive(true);
        ReleaseEntity newReleaseEntity = new ReleaseEntity();
        newReleaseEntity.setName(entityName);
        newReleaseEntity.setActive(true);
        when(releaseRepository.findByNameAndActive(entityName, true)).thenReturn(Optional.of(existingReleaseEntity));
        BusinessException exception = assertThrows(BusinessException.class,
                () -> releaseService.saveRelease(newReleaseEntity));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testGetReleaseByIdExists() {
        UUID releaseId = UUID.randomUUID();
        ReleaseEntity existingReleaseEntity = new ReleaseEntity();
        existingReleaseEntity.setUuid(releaseId);
        existingReleaseEntity.setActive(true);
        when(releaseRepository.findByUuidAndActive(releaseId, true)).thenReturn(Optional.of(existingReleaseEntity));
        ReleaseEntity retrievedReleaseEntity = releaseService.getReleaseById(releaseId);
        assertNotNull(retrievedReleaseEntity);
        assertEquals(releaseId, retrievedReleaseEntity.getUuid());
        assertTrue(retrievedReleaseEntity.isActive());
    }

    @Test
    public void testGetReleaseByIdNotFound() {
        UUID releaseId = UUID.randomUUID();
        when(releaseRepository.findByUuidAndActive(releaseId, true)).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class,
                () -> releaseService.getReleaseById(releaseId));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }


    @Test
    public void testUpdateReleaseSuccess() {
        UUID releaseId = UUID.randomUUID();
        ReleaseEntity existingReleaseEntity = createReleaseEntity1("Existing Release");
        existingReleaseEntity.setUuid(releaseId);
        ReleaseEntity updatedReleaseEntity = createReleaseEntity1("Updated Release");
        updatedReleaseEntity.setUuid(releaseId);
        when(releaseRepository.findByUuidAndActive(releaseId, true)).thenReturn(Optional.of(existingReleaseEntity));
        when(releaseStatusCache.fromCache(updatedReleaseEntity.getReleaseStatus().getName()))
                .thenReturn(updatedReleaseEntity.getReleaseStatus());
        ReleaseEntity updatedEntity = releaseService.updateRelease(releaseId, updatedReleaseEntity);
        assertNotNull(updatedEntity);
        assertEquals("Updated Release", updatedEntity.getName());
    }

    @Test
    public void testUpdateReleaseInvalidStatusChangeMoreThenOne() {
        UUID releaseId = UUID.randomUUID();
        ReleaseEntity existingReleaseEntity = createReleaseEntity1("Existing Release");
        existingReleaseEntity.setUuid(releaseId);
        ReleaseEntity updatedReleaseEntity = createReleaseEntity2("Updated Release");
        updatedReleaseEntity.setUuid(releaseId);
        when(releaseRepository.findByUuidAndActive(releaseId, true)).thenReturn(Optional.of(existingReleaseEntity));
        when(releaseStatusCache.fromCache(updatedReleaseEntity.getReleaseStatus().getName()))
                .thenReturn(ReleaseStatusEntity.builder().name("Status 2").order(4).build());
        BusinessException exception = assertThrows(BusinessException.class,
                () -> releaseService.updateRelease(releaseId, updatedReleaseEntity));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testUpdateReleaseInvalidStatusChangeLessThenOne() {
        UUID releaseId = UUID.randomUUID();
        ReleaseEntity existingReleaseEntity = createReleaseEntity1("Existing Release");
        existingReleaseEntity.setUuid(releaseId);
        ReleaseEntity updatedReleaseEntity = createReleaseEntity2("Updated Release");
        updatedReleaseEntity.setUuid(releaseId);
        when(releaseRepository.findByUuidAndActive(releaseId, true)).thenReturn(Optional.of(existingReleaseEntity));
        when(releaseStatusCache.fromCache(updatedReleaseEntity.getReleaseStatus().getName()))
                .thenReturn(ReleaseStatusEntity.builder().name("Status 2").order(-4).build());
        BusinessException exception = assertThrows(BusinessException.class,
                () -> releaseService.updateRelease(releaseId, updatedReleaseEntity));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testDeleteReleaseSuccess() {
        UUID releaseId = UUID.randomUUID();
        ReleaseEntity existingReleaseEntity = createReleaseEntity1("Existing Release");
        existingReleaseEntity.setUuid(releaseId);
        when(releaseRepository.findByUuidAndActive(releaseId, true)).thenReturn(Optional.of(existingReleaseEntity));
        when(releaseRepository.save(any())).thenReturn(existingReleaseEntity);
        boolean deleted = releaseService.deleteRelease(releaseId);
        assertTrue(deleted);
        assertFalse(existingReleaseEntity.isActive());
    }

    @Test
    public void testDeleteReleaseNotFound() {
        UUID releaseId = UUID.randomUUID();
        when(releaseRepository.findByUuidAndActive(releaseId, true)).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class,
                () -> releaseService.deleteRelease(releaseId));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    private ReleaseEntity createReleaseEntity1(String name) {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setName(name);
        releaseEntity.setActive(true);
        releaseEntity.setReleaseStatus(ReleaseStatusEntity.builder()
                        .name("Status 1")
                        .order(0)
                .build());
        return releaseEntity;
    }

    private ReleaseEntity createReleaseEntity2(String name) {
        ReleaseEntity releaseEntity = new ReleaseEntity();
        releaseEntity.setName(name);
        releaseEntity.setActive(true);
        releaseEntity.setReleaseStatus(ReleaseStatusEntity.builder()
                .name("Status 2")
                .order(1)
                .build());
        return releaseEntity;
    }

}