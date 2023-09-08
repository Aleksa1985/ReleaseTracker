package com.lexsoft.releasetracker.fascade;

import com.lexsoft.releasetracker.dto.ReleaseDto;
import com.lexsoft.releasetracker.dto.ReleaseWrapper;

import java.text.ParseException;
import java.util.UUID;

public interface ReleaseFascade {

    ReleaseDto updateRelease(UUID id, ReleaseDto releaseDto);

    ReleaseDto saveRelease(ReleaseDto releaseDto) ;

    ReleaseWrapper getReleases(String name,
                               String description,
                               String releaseDateFrom,
                               String releaseDateTo,
                               String status,
                               Integer page,
                               Integer size);

    ReleaseDto getRelease(UUID id);

    boolean deleteRelease(UUID id);


}
