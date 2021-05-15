package com.github.xsi640.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.transaction.annotation.Transactional

interface Service<M : Any, ID> {
    fun page(
        page: Int = 1,
        size: Int = 100,
        order: OrderMode = OrderMode.DESC,
        vararg sort: String = emptyArray()
    ): Page<M>

    fun list(order: OrderMode = OrderMode.DESC, vararg sort: String = emptyArray()): List<M>
    fun get(id: ID): M?
    fun save(entity: M): M
    fun delete(id: ID)
    fun delete(ids: List<ID>)
}

enum class OrderMode {
    ASC, DESC
}

abstract class BaseService<M : Any, ID> : Service<M, ID> {

    @Autowired
    protected lateinit var repository: BaseRepository<M, ID>

    override fun page(page: Int, size: Int, order: OrderMode, vararg sort: String): Page<M> {
        val pageRequest = if (sort.isEmpty()) {
            PageRequest.of(page, size)
        } else {
            PageRequest.of(page, size, if (order == OrderMode.DESC) Sort.Direction.DESC else Sort.Direction.ASC)
        }
        return repository.findAll(pageRequest)
    }

    override fun list(order: OrderMode, vararg sort: String): List<M> {
        return if (sort.isEmpty()) {
            repository.findAll()
        } else {
            repository.findAll(Sort.by(if (order == OrderMode.DESC) Sort.Direction.DESC else Sort.Direction.ASC, *sort))
        }
    }

    override fun get(id: ID): M? {
        return repository.getOne(id)
    }

    @Transactional
    override fun save(entity: M): M {
        return repository.saveAndFlush(entity)
    }

    @Transactional
    override fun delete(id: ID) {
        repository.deleteById(id)
    }

    @Transactional
    override fun delete(ids: List<ID>) {
        ids.forEach { delete(it) }
    }
}
