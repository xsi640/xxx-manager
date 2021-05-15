package com.github.xsi640.core

import org.springframework.core.MethodParameter
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

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
    PARAMETER_ERROR(400),
    NOT_FOUND(404),
    NO_PERMISSION(403),
    UN_AUTHORIZED(401),
    UNKNOWN(500);

    companion object {
        fun codeOf(code: Int): ResponseStatus {
            return values().first { it.code == code }
        }
    }
}

@ControllerAdvice
class GlobalControllerAdvice : ResponseBodyAdvice<Any> {

    companion object {
        const val API_URL = "/api/"
    }

    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun exceptionHandler(e: Exception): Response<*> {
        return Response.of(
            body = null,
            message = if (e.message.isNullOrEmpty()) "" else e.message!!,
            status = ResponseStatus.UNKNOWN
        )
    }

    @ExceptionHandler(ResponseException::class)
    @ResponseBody
    fun responseExceptionHandler(e: ResponseException): Response<*> {
        return Response.of(
            body = null,
            message = e.message,
            status = e.status
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseBody
    fun parameterExceptionHandler(e: MethodArgumentNotValidException): Response<*> {
        val exceptions = e.bindingResult
        val msg = if (exceptions.hasErrors()) {
            exceptions.allErrors.filter { it.defaultMessage != null && it.defaultMessage!!.isNotEmpty() }
                .map { it.defaultMessage }.joinToString(", ")
        } else {
            ""
        }
        return Response.of(
            body = null,
            message = msg,
            status = ResponseStatus.PARAMETER_ERROR
        )
    }

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val url = request.uri
        if (!url.path.startsWith(API_URL)) {
            return body
        }
        if (body is Response<*>) {
            return body
        }
        return Response.of(body)
    }
}