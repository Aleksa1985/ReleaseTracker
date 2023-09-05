package com.lexsoft.releasetracker.repository;

import com.lexsoft.releasetracker.repository.model.ReleaseStatusEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseStatusRepository extends JpaRepository<ReleaseStatusEntity, Long> {

    ReleaseStatusEntity findByNameAndActive(String name, boolean active);
    List<ReleaseStatusEntity> findByActive(boolean active);

}
