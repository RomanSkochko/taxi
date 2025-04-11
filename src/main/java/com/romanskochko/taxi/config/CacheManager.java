package com.romanskochko.taxi.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.cache.Cache;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class CacheManager {
    org.springframework.cache.CacheManager cacheManager;

    public void evictUserById(String userId) {
        evictCache("userById", userId);
    }

    public void evictUserByPhone(String phone) {
        evictCache("userDetails", phone);
    }

    public void evictAllUsers() {
        evictCache("userDetails", null);
    }

    private void evictCache(String cacheName, Object key) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            if (key == null) {
                cache.clear();
            } else {
                cache.evict(key);
            }
        }
    }
}
