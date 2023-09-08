package com.lexsoft.releasetracker.service.impl;

import com.lexsoft.releasetracker.repository.ReleaseStatusRepository;
import com.lexsoft.releasetracker.repository.cache.ReleaseStatusCache;
import com.lexsoft.releasetracker.repository.model.ReleaseStatusEntity;
import com.lexsoft.releasetracker.service.ReleaseStatusService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.h2.util.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReleaseStatusServiceImpl implements ReleaseStatusService {

    private final ReleaseStatusCache releaseStatusCache;
    private final ReleaseStatusRepository releaseStatusRepository;

    /**
     * Populate database and cache with statuses from status.csv file
     */
    @PostConstruct
    protected void initialize() {
        ClassPathResource resource = new ClassPathResource("status.csv");
        loadDataFromFile(resource);
    }

    @Override
    @Transactional
    public ReleaseStatusEntity save(ReleaseStatusEntity entity) {
        return Optional.ofNullable(releaseStatusCache.fromCache(entity.getName()))
                .orElseGet(() -> {
                    ReleaseStatusEntity save = releaseStatusRepository.save(entity);
                    releaseStatusCache.toCache(save);
                    return save;
                });
    }

    @Override
    public ReleaseStatusEntity get(String name) {
        return releaseStatusCache.fromCache(name);
    }


    @Transactional
    protected String loadDataFromFile(ClassPathResource resource){
        InputStream inputStream;
        try {
            inputStream = resource.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String record;
            AtomicReference<ReleaseStatusEntity> previous = new AtomicReference<>();
            while ((record = reader.readLine()) != null) {
                String[] split = record.split(",");
                if (split.length == 2 && StringUtils.isNumber(split[1])) {
                    Optional.ofNullable(previous.get()).ifPresentOrElse(p -> {
                        if (previous.get().getOrder() < Integer.valueOf(split[1]) && Objects.isNull(releaseStatusCache.fromCache(split[0]))) {
                            ReleaseStatusEntity entity = ReleaseStatusEntity.builder()
                                    .name(split[0])
                                    .order(Integer.valueOf(split[1]))
                                    .build();
                            save(entity);
                            previous.set(entity);
                        } else {
                            log.error("Invalid order of status elements, or element with that status already exist.");
                        }
                    }, () -> {
                        ReleaseStatusEntity entity = ReleaseStatusEntity.builder()
                                .name(split[0])
                                .order(Integer.valueOf(split[1]))
                                .build();
                        save(entity);
                        previous.set(entity);
                    });
                } else {
                    log.info("Release status row is corrupted. Status will not be loaded");
                }
            }
        } catch (IOException e) {
            log.error("UNABLE TO LOAD FILE WITH STATUSES.", e);
        }
        String result = "Imported statuses are: " + releaseStatusCache.getAllKeys().stream()
                .collect(Collectors.joining(","));
        log.info(result);
        return result;
    }



}
