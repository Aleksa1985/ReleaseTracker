package com.lexsoft.releasetracker.repository;

import com.lexsoft.releasetracker.repository.model.ReleaseEntity;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReleaseRepository extends JpaRepository<ReleaseEntity, Long> {

    @Query("SELECT e FROM ReleaseEntity e WHERE e.releaseDate BETWEEN :startDate AND :endDate " +
            "AND (e.name LIKE %:name% OR e.description LIKE %:description%) " +
            "AND e.releaseStatus.name = :status AND e.active = true AND e.releaseStatus.active = true")
    Page<ReleaseEntity> findByReleaseDateBetweenAndNameContainingAndDescriptionContaining(
            Date startDate, Date endDate, String name, String description, String status,  Pageable pageable
    );



}
