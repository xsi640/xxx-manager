package com.github.xsi640.auth

import com.github.xsi640.auth.controller.CurrentUserContext
import com.github.xsi640.auth.repository.QUser
import com.github.xsi640.auth.repository.TokenStoreService
import com.github.xsi640.auth.repository.User
import com.github.xsi640.core.BaseService
import com.github.xsi640.core.JsonExtensions.toJson
import com.github.xsi640.core.Response
import com.github.xsi640.core.ResponseException
import com.github.xsi640.core.wildcardMatch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthorizationFilter : Filter {

    @Autowired
    private lateinit var userService: BaseService<User, Long>

    @Autowired
    private lateinit var tokenStoreService: TokenStoreService

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val url = httpServletRequest.requestURI
        if (!url.wildcardMatch(FILTER_ALL)) {
            chain.doFilter(request, response)
            return
        }

        if (ignoreUri(url)) {
            chain.doFilter(request, response)
            return
        }

        try {
            var token = httpServletRequest.getHeader(TOKEN_HEADER)
            if (token.isNullOrEmpty()) {
                token = httpServletRequest.getParameter(TOKEN_PARAMETER_NAME)
            }
            if (token.isNullOrEmpty()) {
                throw UnauthorizedException("Token not exists.")
            }
            val user = findUserByToken(token.removePrefix()) ?: throw UnauthorizedException("Token not exists.")
            request.setAttribute(CurrentUserContext.REMOTE_USER, user.username)
            request.setAttribute(REQUEST_USER_KEY, user)
        } catch (e: ResponseException) {
            val httpServletResponse = response as HttpServletResponse
            httpServletResponse.status = e.status.code
            httpServletResponse.writer.write(
                Response.of(e.status, e.message).toJson()
            )
            return
        }
        chain.doFilter(request, response)
    }

    private fun findUserByToken(token: String): User? {
        val jwtUser = token.toJwtUser()
        if (jwtUser.userId.isEmpty()) {
            throw TokenErrorException()
        }
        val prevToken = tokenStoreService.get(jwtUser.userId)
        if (prevToken == null || prevToken != token) {
            throw TokenErrorException()
        }
        val user = userService.jpaQuery.where(QUser.user.id.eq(jwtUser.userId.toLong())).fetchOne() ?: return null
        if (user.username != jwtUser.username) {
            throw UnauthorizedException()
        }
        return user
    }

    private fun ignoreUri(url: String): Boolean {
        for (s in AUTH_WHITELIST) {
            if (url.contains(s)) {
                return true
            }
        }
        return false
    }
}