package com.github.xsi640.auth.repository

import com.github.xsi640.auth.CACHE_PREFIX
import com.github.xsi640.auth.TOKEN_EXPIRATION
import com.github.xsi640.core.CacheBuilder
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class TokenStoreService {
    fun set(userId: String, token: String) {
        cache.put(CACHE_PREFIX + userId, token)
    }

    fun get(userId: String): String? {
        return cache.getIfPresent(CACHE_PREFIX + userId)
    }

    fun delete(userId: String) {
        cache.invalidate(CACHE_PREFIX + userId)
    }

    companion object {
        private val cache =
            CacheBuilder.newBuilder().expireAfterWrite(TOKEN_EXPIRATION.toLong(), TimeUnit.SECONDS)
                .build<String, String>()
    }
}