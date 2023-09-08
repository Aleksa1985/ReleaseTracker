package com.lexsoft.releasetracker.repository;

import com.lexsoft.releasetracker.repository.model.ReleaseEntity;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReleaseRepository extends JpaRepository<ReleaseEntity, Long> {

    @Query("SELECT e FROM ReleaseEntity e WHERE e.releaseDate BETWEEN :startDate AND :endDate " +
            "OR e.name LIKE %:name% OR e.description LIKE %:description% " +
            "OR e.releaseStatus.name = :status AND e.active = true AND e.releaseStatus.active = true")
    Page<ReleaseEntity> findByReleaseDateBetweenAndNameContainingAndDescriptionContaining(
            Date startDate, Date endDate, String name, String description, String status,  Pageable pageable
    );

    Optional<ReleaseEntity> findByNameAndActive(String name, boolean active);
    Optional<ReleaseEntity> findByUuidAndActive(UUID uuid, boolean active);



}
