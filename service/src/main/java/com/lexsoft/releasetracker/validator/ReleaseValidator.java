package com.lexsoft.releasetracker.validator;

import com.lexsoft.releasetracker.dto.ReleaseDto;
import com.lexsoft.releasetracker.dto.ReleaseDtoSearch;

public interface ReleaseValidator {

    public void validateReleaseDto(ReleaseDto releaseDto);
    public void validateReleaseDtoSearch(ReleaseDtoSearch releaseDtoSearch);


}
