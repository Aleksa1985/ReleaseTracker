package com.lexsoft.releasetracker.service.impl;

import com.lexsoft.releasetracker.exception.BusinessException;
import com.lexsoft.releasetracker.exception.model.ErrorMessage;
import com.lexsoft.releasetracker.repository.ReleaseRepository;
import com.lexsoft.releasetracker.repository.cache.ReleaseStatusCache;
import com.lexsoft.releasetracker.repository.model.ReleaseEntity;
import com.lexsoft.releasetracker.repository.model.ReleaseStatusEntity;
import com.lexsoft.releasetracker.service.ReleaseService;
import com.lexsoft.releasetracker.utils.DateUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReleaseServiceImpl implements ReleaseService {

    private final ReleaseStatusCache releaseStatusCache;
    private final ReleaseRepository releaseRepository;

    @Override
    @Transactional
    public ReleaseEntity saveRelease(ReleaseEntity releaseEntity) {
        if (releaseRepository.findByNameAndActive(releaseEntity.getName(), true).isPresent()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    List.of(ErrorMessage.builder().message(
                                    String.format("Active Release with name %s alredy exist.", releaseEntity.getName()))
                            .build()));
        }
        ReleaseStatusEntity releaseStatusEntity = releaseStatusCache.fromCache(releaseEntity.getReleaseStatus().getName());
        if(releaseStatusEntity.getOrder() != 0){
            throw new BusinessException(HttpStatus.BAD_REQUEST, List.of(ErrorMessage.builder()
                    .message("Release can be created only in 'Created' status.").build()));
        }
        releaseEntity.setReleaseStatus(releaseStatusEntity);
        return releaseRepository.save(releaseEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public ReleaseEntity getReleaseById(UUID releaseId) {
        return releaseRepository.findByUuidAndActive(releaseId, true)
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
                        List.of(ErrorMessage.builder().message(
                                        String.format("Active Release with id %s does not exist.", releaseId))
                                .build())));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReleaseEntity> getReleases(String name,
                                           String description,
                                           String releaseDateFrom,
                                           String releaseDateTo,
                                           String status,
                                           Integer page,
                                           Integer size) {

        Pageable pageable = PageRequest.of(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(100));
        return releaseRepository.findByReleaseDateBetweenAndNameContainingAndDescriptionContaining(
                releaseDateFrom != null ? DateUtils.stringToDate(releaseDateFrom) : null,
                releaseDateTo != null ? DateUtils.stringToDate(releaseDateTo) : null,
                name, description, status, pageable);

    }

    @Override
    @Transactional
    public ReleaseEntity updateRelease(UUID releaseId, ReleaseEntity releaseEntity) {
        return releaseRepository.findByUuidAndActive(releaseId, true)
                .map(releaseDB -> {
                    ReleaseStatusEntity newReleaseStatus = releaseStatusCache.fromCache(releaseEntity.getReleaseStatus().getName());
                    if (newReleaseStatus.getOrder() == releaseDB.getReleaseStatus().getOrder() + 1 || // next status.
                            newReleaseStatus.getOrder() == releaseDB.getReleaseStatus().getOrder() - 1 || // previous status.
                            newReleaseStatus.getOrder() == releaseDB.getReleaseStatus().getOrder()) {     // status stays the same.
                        releaseRepository.findByNameAndActive(releaseEntity.getName(), true).ifPresent(e -> {
                            if (!releaseDB.getName().equals(e.getName())) {
                                throw new BusinessException(HttpStatus.BAD_REQUEST,
                                        List.of(ErrorMessage.builder().message(
                                                        String.format("Release Name is already used.", releaseId))
                                                .build()));
                            }
                        });
                        releaseDB.setReleaseDate(releaseEntity.getReleaseDate());
                        releaseDB.setName(releaseEntity.getName());
                        releaseDB.setDescription(releaseEntity.getDescription());
                        releaseDB.setReleaseStatus(newReleaseStatus);
                        releaseRepository.save(releaseDB);
                        return releaseDB;
                    } else {
                        throw new BusinessException(HttpStatus.BAD_REQUEST,
                                List.of(ErrorMessage.builder().message(
                                                String.format("Release status can only be moved to the next one or one before.", releaseId))
                                        .build()));
                    }
                }).orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
                        List.of(ErrorMessage.builder().message(
                                        String.format("Active Release with id %s does not exist.", releaseId))
                                .build())));
    }

    @Override
    @Transactional
    public boolean deleteRelease(UUID releaseId) {
        return releaseRepository.findByUuidAndActive(releaseId, true)
                .map(releaseEntity -> {
                    releaseEntity.setActive(false);
                    releaseRepository.save(releaseEntity);
                    return true;
                })
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
                        List.of(ErrorMessage.builder().message(
                                        String.format("Active Release with id %s does not exist.", releaseId))
                                .build())));
    }

}
