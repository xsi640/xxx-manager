package com.github.xsi640.core

import org.springframework.data.domain.Page

data class Response<T : Any?>(
    val status: Int = 0,
    val message: String = "",
    val body: T? = null
) {
    companion object {

        val OK = of(null)

        val NOT_FOUND = of(null, "not found", ResponseStatus.NOT_FOUND)

        fun <T : Any?> of(
            body: T?,
            message: String = "success",
            status: ResponseStatus = ResponseStatus.OK
        ): Response<T> {
            return Response(
                status = status.code,
                message = message,
                body = body
            )
        }
    }
}

data class Paged<T : Any>(
    val total: Long,
    val totalPages: Int,
    val page: Int,
    val size: Int,
    val items: List<T>
) {
    companion object {
        fun <T : Any> of(p: Page<T>): Response<Paged<T>> {
            return Response.of(
                Paged(
                    total = p.totalElements,
                    totalPages = p.totalPages,
                    page = p.number + 1,
                    size = p.size,
                    items = p.content
                )
            )
        }

        fun <T : Any> of(items: List<T>, page: Int, size: Int, total: Long): Response<Paged<T>> {
            return Response.of(
                Paged(
                    total = total,
                    totalPages = if (total % size == 0L) {
                        (total / size).toInt()
                    } else {
                        (total / size + 1).toInt()
                    },
                    page = page,
                    size = size,
                    items = items
                )
            )
        }
    }
}

enum class ResponseStatus(val code: Int) {
    OK(0),
    NOT_FOUND(404);

    companion object {
        fun codeOf(code: Int): ResponseStatus {
            return values().first { it.code == code }
        }
    }
}