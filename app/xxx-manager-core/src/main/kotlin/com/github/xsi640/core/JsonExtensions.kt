package com.github.xsi640.core

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.File

object JsonExtensions {
    private val mapper = jacksonObjectMapper()

    init {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false)
    }

    fun <T> String.fromJson(clazz: Class<T>): T {
        return mapper.readValue(this, clazz)
    }

    fun <T> String.fromJson(javaType: JavaType): T {
        return mapper.readValue(this, javaType)
    }

    fun <T> String.fromJson(collectionClass: Class<out Collection<*>>, elementClass: Class<T>): List<T> {
        val javaType = mapper.typeFactory.constructCollectionType(collectionClass, elementClass)
        return this.fromJson(javaType)
    }

    fun <T> String.fromJsonWithGeneric(clazz: Class<T>, vararg genericClass: Class<*>): T {
        val javaType = mapper.typeFactory.constructParametricType(clazz, *genericClass)
        return this.fromJson(javaType)
    }

    fun String.toJsonNode(): JsonNode {
        return mapper.readTree(this)
    }

    fun Any.toJson(): String {
        return mapper.writeValueAsString(this)
    }

    fun Any.toJsonFile(file: File) {
        mapper.writeValue(file, this)
    }
}