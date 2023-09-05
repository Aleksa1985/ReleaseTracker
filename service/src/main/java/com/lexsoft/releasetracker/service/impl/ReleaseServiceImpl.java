package com.lexsoft.releasetracker.service.impl;

import com.lexsoft.releasetracker.exception.BusinessException;
import com.lexsoft.releasetracker.exception.model.ErrorMessage;
import com.lexsoft.releasetracker.repository.ReleaseRepository;
import com.lexsoft.releasetracker.repository.model.ReleaseEntity;
import com.lexsoft.releasetracker.service.ReleaseService;
import com.lexsoft.releasetracker.utils.DateUtils;

import java.text.ParseException;
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

    private final ReleaseRepository releaseRepository;

    @Override
    @Transactional
    public ReleaseEntity saveRelease(ReleaseEntity releaseEntity) {
        return releaseRepository.save(releaseEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public ReleaseEntity getReleaseById(UUID releaseId) {
        return null;
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

        // TODO: push paging default values inside configuration
        Pageable pageable = PageRequest.of(
                Optional.ofNullable(page).orElse(0),
                Optional.ofNullable(size).orElse(50));
        try {
            return releaseRepository.findByReleaseDateBetweenAndNameContainingAndDescriptionContaining(
                    DateUtils.stringToDate(releaseDateFrom),
                    DateUtils.stringToDate(releaseDateTo),
                    name, description, status, pageable);
        } catch (ParseException e) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, List.of(ErrorMessage.builder()
                            .code(1000)
                            .message("Invalid format for field values for releaseDateFrom and/or releaseDateTo.")
                    .build()));
        }
    }

    @Override
    @Transactional
    public ReleaseEntity updateRelease(ReleaseEntity releaseEntity) {
        return null;
    }

    @Override
    @Transactional
    public boolean deleteRelease(UUID releaseId) {
        return false;
    }
}
