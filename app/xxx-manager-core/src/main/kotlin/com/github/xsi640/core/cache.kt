package com.github.xsi640.core

import com.github.benmanes.caffeine.cache.Caffeine
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.TimeUnit
import java.util.function.Function


interface Cache<K : Any, V : Any> {
    fun getIfPresent(key: K): V?
    fun get(key: K, mappingFunction: Function<in K, out V?>): V?
    fun getAllPresent(keys: Iterable<K>): Map<K, V>
    fun put(key: K, value: V)
    fun pullAll(map: Map<K, V>)
    fun invalidate(key: K)
    fun invalidateAll(keys: Iterable<K>)
    fun invalidateAll()
    fun estimatedSize(): Long
    fun asMap(): ConcurrentMap<K, V>
}

interface CacheBuilder {
    fun expireAfterWrite(duration: Long, unit: TimeUnit): CacheBuilder
    fun expireAfterAccess(duration: Long, unit: TimeUnit): CacheBuilder
    fun maximumSize(maximumSize: Long): CacheBuilder
    fun <K : Any, V : Any> build(): Cache<K, V>

    companion object {
        fun newBuilder(): CacheBuilderImpl {
            return CacheBuilderImpl()
        }
    }
}

class CacheImpl<K : Any, V : Any>(
    val cache: com.github.benmanes.caffeine.cache.Cache<K, V>
) : Cache<K, V> {
    override fun getIfPresent(key: K): V? {
        return cache.getIfPresent(key)
    }

    override fun get(key: K, mappingFunction: Function<in K, out V?>): V? {
        return cache.get(key, mappingFunction)
    }

    override fun getAllPresent(keys: Iterable<K>): Map<K, V> {
        return cache.getAllPresent(keys)
    }

    override fun put(key: K, value: V) {
        cache.put(key, value)
    }

    override fun pullAll(map: Map<K, V>) {
        cache.putAll(map)
    }

    override fun invalidate(key: K) {
        cache.invalidate(key)
    }

    override fun invalidateAll(keys: Iterable<K>) {
        cache.invalidateAll(keys)
    }

    override fun invalidateAll() {
        cache.invalidateAll()
    }

    override fun estimatedSize(): Long {
        return cache.estimatedSize()
    }

    override fun asMap(): ConcurrentMap<K, V> {
        return cache.asMap()
    }
}

class CacheBuilderImpl : CacheBuilder {

    private val cached = Caffeine.newBuilder()

    override fun expireAfterWrite(duration: Long, unit: TimeUnit): CacheBuilder {
        cached.expireAfterWrite(duration, unit)
        return this
    }

    override fun expireAfterAccess(duration: Long, unit: TimeUnit): CacheBuilder {
        cached.expireAfterAccess(duration, unit)
        return this
    }

    override fun maximumSize(maximumSize: Long): CacheBuilder {
        cached.maximumSize(maximumSize)
        return this
    }

    override fun <K : Any, V : Any> build(): Cache<K, V> {
        return CacheImpl(cached.build())
    }

}