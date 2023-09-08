package com.lexsoft.releasetracker.service.impl;

import com.lexsoft.releasetracker.repository.cache.ReleaseStatusCache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ReleaseStatusServiceImplTest {

    @Autowired
    private ReleaseStatusServiceImpl releaseStatusService;
    @Autowired
    private ReleaseStatusCache releaseStatusCache;

    @BeforeEach
    public void before(){
        releaseStatusCache.evictAll();
    }

    @Test
    public void testInitializeSuccess() {
        ClassPathResource resource = new ClassPathResource("test-status.csv");
        String result = releaseStatusService.loadDataFromFile(resource);
        Assertions.assertEquals("Imported statuses are: Done,In Development,On staging,QA Done on DEV,Created,On PROD,On DEV,QA done on STAGING", result);
        Assertions.assertEquals(8 , releaseStatusCache.getAllKeys().size());
    }

    @Test
    public void testInitializeSuccessWithDuplicates() {
        ClassPathResource resource = new ClassPathResource("test-status-duplicates.csv");
        String result = releaseStatusService.loadDataFromFile(resource);
        Assertions.assertEquals("Imported statuses are: Done,In Development,On staging,QA Done on DEV,Created,On PROD,On DEV,QA done on STAGING", result);
        Assertions.assertEquals(8 , releaseStatusCache.getAllKeys().size());
    }
}


