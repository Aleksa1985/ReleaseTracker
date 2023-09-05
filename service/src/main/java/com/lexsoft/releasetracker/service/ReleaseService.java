package com.lexsoft.releasetracker.service;

import com.lexsoft.releasetracker.repository.model.ReleaseEntity;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

public interface ReleaseService {

    public ReleaseEntity saveRelease(ReleaseEntity releaseEntity);
    public ReleaseEntity getReleaseById(UUID releaseId);
    public Page<ReleaseEntity> getReleases(String name,
                                           String description,
                                           String releaseDateFrom,
                                           String releaseDateTo,
                                           String status,
                                           Integer page,
                                           Integer size);
    public ReleaseEntity updateRelease(ReleaseEntity releaseEntity);
    public boolean deleteRelease(UUID releaseId);

}
