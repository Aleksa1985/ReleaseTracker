package com.lexsoft.releasetracker.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReleaseDtoSearchTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void testValidReleaseDtoSearch() {
        ReleaseDtoSearch releaseDtoSearch = new ReleaseDtoSearch();
        releaseDtoSearch.setName("Test Release");
        releaseDtoSearch.setDescription("Test Description");
        releaseDtoSearch.setReleaseDateFrom("01/01/2022");
        releaseDtoSearch.setReleaseDateTo("31/12/2022");
        releaseDtoSearch.setStatus("Created");
        releaseDtoSearch.setPage(1);
        releaseDtoSearch.setSize(10);
        Set<ConstraintViolation<ReleaseDtoSearch>> violations = validator.validate(releaseDtoSearch);
        assertTrue(violations.isEmpty());
    }

}