package com.lexsoft.releasetracker.validator.impl;

import com.lexsoft.releasetracker.dto.ReleaseDto;
import com.lexsoft.releasetracker.dto.ReleaseDtoSearch;
import com.lexsoft.releasetracker.exception.BusinessException;
import com.lexsoft.releasetracker.exception.model.ErrorMessage;
import com.lexsoft.releasetracker.repository.cache.ReleaseStatusCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReleaseReleaseValidator implements com.lexsoft.releasetracker.validator.ReleaseValidator {

    private final ReleaseStatusCache releaseStatusCache;

    /**
     * Validator that should check if data is in some dynamic scope that can not be hardcoded.
     * @param releaseDto Request object from user
     */
    @Override
    public void validateReleaseDto(ReleaseDto releaseDto) {
        List<ErrorMessage> list = new ArrayList<>();
        Optional.ofNullable(releaseStatusCache.fromCache(releaseDto.getStatus()))
                .ifPresentOrElse(status -> {
                }, () -> list.add(ErrorMessage.builder()
                        .code(1001)
                        .message("Invalid Release code. Must be one of these: " + releaseStatusCache.getAllKeys().stream()
                                .collect(Collectors.joining(",")))
                        .build()));
        if (!list.isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, list);
        }
    }

    @Override
    public void validateReleaseDtoSearch(ReleaseDtoSearch releaseDtoSearch) {
        List<ErrorMessage> list = new ArrayList<>();
        Optional.ofNullable(releaseStatusCache.fromCache(releaseDtoSearch.getStatus()))
                .ifPresentOrElse(status -> {
                }, () -> list.add(ErrorMessage.builder()
                        .code(1001)
                        .message("Invalid Release code. Must be one of these: " + releaseStatusCache.getAllKeys().stream()
                                .collect(Collectors.joining(",")))
                        .build()));
        if (!list.isEmpty()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, list);
        }

    }
}
