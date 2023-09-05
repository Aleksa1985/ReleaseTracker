package com.lexsoft.releasetracker.repository.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.lexsoft.releasetracker.repository.ReleaseStatusRepository;
import com.lexsoft.releasetracker.repository.model.ReleaseStatusEntity;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

/**
 * This class will be used for controlling release statuses and holding release statuses in the cache
 * to avoid load over DB level
 */
@Component
public class ReleaseStatusCache {

    private ReleaseStatusRepository releaseStatusRepository;

    private final LoadingCache<String, ReleaseStatusEntity> cache;

    /**
     *@param releaseStatusRepository - JPA repository from spring context
     * Populate and refresh the cache in every 5 minutes on ReleaseStatusCache
     * component creation.
     * TODO: make this cache distributed.
     * TODO: make time of expire configurable.
     */
    public ReleaseStatusCache(ReleaseStatusRepository releaseStatusRepository) {
        this.releaseStatusRepository = releaseStatusRepository;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(key -> releaseStatusRepository.findByNameAndActive(key, true));
        releaseStatusRepository.findAll().forEach(status -> toCache(status));
    }

    /**
     * @param name - status name
     * @return ReleaseStatusEntity object from cache
     */
    public ReleaseStatusEntity fromCache(String name){
        return cache.get(name);
    }

    /**
     * @param statusEntity - statusEntity object
     * @return ReleaseStatusEntity object from cache
     */
    public void toCache(ReleaseStatusEntity statusEntity){
         cache.put(statusEntity.getName(), statusEntity);
    }

    /**
     * @return all possible statuses.
     */
    public Set<String> getAllKeys(){
        return cache.asMap().keySet();
    }

}
