package com.github.xsi640.auth

import com.github.xsi640.core.ResponseException
import com.github.xsi640.core.ResponseStatus

open class UnauthorizedException(
    override val message: String = "",
    override val status: ResponseStatus = ResponseStatus.UN_AUTHORIZED
) : ResponseException(status, message)

open class TokenErrorException : ResponseException(ResponseStatus.UN_AUTHORIZED, "token error")