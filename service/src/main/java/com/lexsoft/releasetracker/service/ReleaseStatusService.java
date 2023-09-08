package com.lexsoft.releasetracker.service;

import com.lexsoft.releasetracker.repository.model.ReleaseStatusEntity;

public interface ReleaseStatusService {

        ReleaseStatusEntity save(ReleaseStatusEntity entity);
        ReleaseStatusEntity get(String name);


}
