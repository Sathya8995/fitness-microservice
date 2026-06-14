package com.fitness.userservice.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableCaching
@Profile("local")
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        // Uses ConcurrentHashMap with unlimited size
        // For limited size, use Caffeine (requires dependency)
        return new ConcurrentMapCacheManager("users");
    }
}

