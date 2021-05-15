package com.github.xsi640.core

open class ResponseException(
    open val status: ResponseStatus,
    override val message: String = ""
) : Exception()

open class NotFoundException(
    override val message: String = "",
    override val status: ResponseStatus = ResponseStatus.NOT_FOUND
) : ResponseException(status, message)

open class ParameterException(
    override val message: String = "",
    override val status: ResponseStatus = ResponseStatus.PARAMETER_ERROR
) : ResponseException(status, message)