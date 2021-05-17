package com.github.xsi640.core

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import javax.persistence.EntityManager

@NoRepositoryBean
interface BaseRepository<T, ID> : JpaRepository<T, ID>

@Configuration
class RepositoryConfiguration {
    @Bean
    fun jpaQueryFactory(entityManager: EntityManager): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}