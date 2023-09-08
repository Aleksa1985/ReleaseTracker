package com.lexsoft.releasetracker.repository.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected T id;

	@Column(columnDefinition = "BINARY(16)", unique = true)
	private UUID uuid;

	protected Date created;
	protected Date updated;

	@PrePersist
	protected void onCreate() {
		created = new Date();
		updated = new Date();
		uuid = UUID.randomUUID();
		active = true;
	}

	@PreUpdate
	protected void onUpdate() {
		updated = new Date();
	}

	public boolean active;

}