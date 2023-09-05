package com.lexsoft.releasetracker.repository.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "release", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "active"})}, indexes = {@Index(columnList = {"active", })})
@Index(name = "idx_active", columnList = "active")

@NoArgsConstructor
@AllArgsConstructor
public class ReleaseEntity extends BaseEntity<Long> {

    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne
    private ReleaseStatusEntity releaseStatus;

    private Date releaseDate;

    @Builder
    public ReleaseEntity(Long id, UUID uuid, Date created, Date updated, boolean active, UUID id1, String name, String description, ReleaseStatusEntity releaseStatus, Date releaseDate) {
        super(id, uuid, created, updated, active);
        this.name = name;
        this.description = description;
        this.releaseStatus = releaseStatus;
        this.releaseDate = releaseDate;
    }
}
