package com.github.xsi640.core

open class ResponseException(
    open val status: ResponseStatus,
    override val message: String = ""
) : Exception()

open class NotFoundException(
    override val status: ResponseStatus = ResponseStatus.NOT_FOUND,
    override val message: String = ""
) : ResponseException(status, message)