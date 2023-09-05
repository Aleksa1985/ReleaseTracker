package com.lexsoft.releasetracker.repository.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
@Table(name = "status", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "active", "ordering"})})
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseStatusEntity extends BaseEntity<Long> {

    @OneToMany(mappedBy = "releaseStatus", fetch = FetchType.LAZY)
    List<ReleaseEntity> releaseEntityList;

    String name;

    @Column(name = "ordering")
    Integer order;

    @Builder
    public ReleaseStatusEntity(Long id, UUID uuid, Date created, Date updated, boolean active, List<ReleaseEntity> releaseEntityList, String name, Integer order) {
        super(id, uuid, created, updated, active);
        this.releaseEntityList = releaseEntityList;
        this.name = name;
        this.order = order;
    }
}
