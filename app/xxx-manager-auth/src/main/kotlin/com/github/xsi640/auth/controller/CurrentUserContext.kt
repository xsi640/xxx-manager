package com.github.xsi640.auth.controller

import com.github.xsi640.auth.REQUEST_USER_KEY
import com.github.xsi640.auth.UnauthorizedException
import com.github.xsi640.auth.repository.User
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest

interface CurrentUserContext {
    val request: HttpServletRequest?
    fun currentUser(): User

    companion object {
        const val REMOTE_USER = "X-Remote-User"
    }
}

@Service
class CurrentUserContextImpl : CurrentUserContext {

    override val request: HttpServletRequest?
        get() {
            if (RequestContextHolder.getRequestAttributes() is ServletRequestAttributes) {
                val attrs = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
                return attrs.request
            }
            return null
        }

    override fun currentUser(): User {
        var user: User? = null
        if (request != null) {
            user = request!!.getAttribute(REQUEST_USER_KEY) as User
        }
        if (user == null) {
            throw UnauthorizedException()
        }
        return user
    }
}