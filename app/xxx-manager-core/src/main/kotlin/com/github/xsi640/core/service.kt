package com.github.xsi640.core

import com.querydsl.core.types.*
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.*
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.transaction.annotation.Transactional
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import javax.annotation.PostConstruct

interface Service<M : Any, ID : Any> {
    fun list(page: Long, count: Long, sort: String?, order: OrderMode?, vararg o: Predicate?): Response<Paged<M>>
    fun list(page: Long, count: Long, sort: String?, order: OrderMode?): Response<Paged<M>> {
        return list(page, count, sort, order, null)
    }

    fun listAll(sort: String?, order: OrderMode?, vararg o: Predicate?): List<M>
    fun listAll(sort: String?, order: OrderMode?): List<M> {
        return listAll(sort, order, null)
    }

    fun get(id: ID): M?
    fun findAllById(ids: List<ID>): List<M>
    fun insert(data: M): M
    fun insert(datas: List<M>): List<M>
    fun update(data: M, id: ID): M
    fun delete(id: ID)
    fun delete(id: List<ID>)
    fun count(): Long
    val jpaQuery: JPAQuery<M>
    val jpaDelete: JPADeleteClause
    val jpaInsert: JPAInsertClause
    val jpaUpdate: JPAUpdateClause
}

enum class OrderMode {
    ASC, DESC
}


abstract class BaseService<M : Any, ID: Any> : Service<M, ID> {
    @Autowired
    protected lateinit var repository: BaseRepository<M, ID>

    @Autowired
    protected lateinit var queryFactory: JPAQueryFactory

    private lateinit var entityPath: EntityPath<*>

    private lateinit var moduleClass: Class<*>

    @PostConstruct
    fun initialize() {
        val parameterizedType = this.javaClass.genericSuperclass as ParameterizedType
        val type = parameterizedType.actualTypeArguments[0]
        moduleClass = type as Class<*>
        this.initializeJpaDSL()
    }

    override fun list(page: Long, count: Long, sort: String?, order: OrderMode?, vararg o: Predicate?): Response<Paged<M>> {
        @Suppress("UNCHECKED_CAST")
        val path = if (sort != null && sort.isNotEmpty()) {
            Expressions.path(this.moduleClass, this.entityPath, sort)
        } else {
            null
        } as Path<out Comparable<M>>?
        val query = if (path != null) {
            if (OrderMode.ASC == order) {
                jpaQuery.where(*o).orderBy(OrderSpecifier(Order.ASC, path)).offset((page - 1) * count).limit(count)
                    .fetchResults()
            } else {
                jpaQuery.where(*o).orderBy(OrderSpecifier(Order.DESC, path)).offset((page - 1) * count).limit(count)
                    .fetchResults()
            }
        } else {
            jpaQuery.where(*o).offset((page - 1) * count).limit(count).fetchResults()
        }
        return Paged.of(query.results, page, count, query.total)
    }

    override fun listAll(sort: String?, order: OrderMode?, vararg o: Predicate?): List<M> {
        @Suppress("UNCHECKED_CAST")
        val path = if (sort != null && sort.isNotEmpty()) {
            Expressions.path(this.moduleClass, this.entityPath, sort)
        } else {
            null
        } as Path<out Comparable<M>>?
        val query = if (path != null) {
            if (OrderMode.ASC == order) {
                jpaQuery.where(*o).orderBy(OrderSpecifier(Order.ASC, path)).fetchResults()
            } else {
                jpaQuery.where(*o).orderBy(OrderSpecifier(Order.DESC, path)).fetchResults()
            }
        } else {
            jpaQuery.where(*o).fetchResults()
        }
        return query.results
    }

    override fun get(id: ID): M? {
        return repository.findById(id).orElse(null)
    }

    override fun findAllById(ids: List<ID>): List<M> {
        return repository.findAllById(ids)
    }

    override fun insert(data: M): M {
        return repository.saveAndFlush(data)
    }

    override fun insert(datas: List<M>): List<M> {
        val entities = repository.saveAll(datas)
        repository.flush()
        return entities
    }

    override fun update(data: M, id: ID): M {
        return repository.saveAndFlush(data)
    }

    override fun delete(id: ID) {
        repository.deleteById(id)
    }

    @Transactional
    override fun delete(id: List<ID>) {
        id.forEach { this.delete(it) }
    }

    override fun count(): Long {
        return repository.count()
    }

    @Suppress("UNCHECKED_CAST")
    override val jpaQuery: JPAQuery<M>
        get() = queryFactory.from(this.entityPath) as JPAQuery<M>
    override val jpaDelete: JPADeleteClause
        get() = queryFactory.delete(this.entityPath)
    override val jpaInsert: JPAInsertClause
        get() = queryFactory.insert(this.entityPath)
    override val jpaUpdate: JPAUpdateClause
        get() = queryFactory.update(this.entityPath)

    private fun initializeJpaDSL() {
        val package0 = this.moduleClass.`package`.name
        val className = "Q" + this.moduleClass.simpleName
        val c = Class.forName("$package0.$className")
        val fields = c.declaredFields
        for (field in fields) {
            if (Modifier.isStatic(field.modifiers) &&
                field.name.equals(this.moduleClass.simpleName, true)
            ) {
                this.entityPath = field.get(c) as EntityPath<*>
            }
        }
    }

    companion object {
        val log: Logger by logger()
    }
}