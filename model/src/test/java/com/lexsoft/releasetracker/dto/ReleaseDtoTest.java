package com.lexsoft.releasetracker.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReleaseDtoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void testValidReleaseDto() {
        System.out.println(UUID.randomUUID());
        ReleaseDto releaseDto = ReleaseDto.builder()
                .id("123e4567-e89b-12d3-1111-426655440000")
                .name("Release Name")
                .description("Release Description")
                .status("Created")
                .releaseDate("12/12/2024")
                .createdAt("2022-01-01T00:00:00Z")
                .lastUpdateAt("2022-01-02T00:00:00Z")
                .build();
        Set<ConstraintViolation<ReleaseDto>> violations = validator.validate(releaseDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidReleaseDto() {
        ReleaseDto releaseDto = ReleaseDto.builder().build();

        Set<ConstraintViolation<ReleaseDto>> violations = validator.validate(releaseDto);
        assertFalse(violations.isEmpty());
        assertEquals(4, violations.size());

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("releaseDate")));
    }
}