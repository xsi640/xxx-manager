package com.github.xsi640.auth.controller

import com.github.xsi640.auth.*
import com.github.xsi640.auth.repository.*
import com.github.xsi640.core.BaseService
import com.github.xsi640.core.NotFoundException
import org.hibernate.validator.constraints.Length
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {
    @Autowired
    private lateinit var userService: BaseService<User, Long>

    @Autowired
    private lateinit var refreshTokenService: BaseService<RefreshToken, String>

    @Autowired
    private lateinit var tokenStoreService: TokenStoreService

    @Autowired
    private lateinit var currentUserContext: CurrentUserContext

    @PostMapping("login")
    fun login(@Valid @RequestBody request: LoginRequest): JwtToken {
        val user = userService.jpaQuery.where(QUser.user.username.eq(request.username)).fetchOne()
            ?: throw NotFoundException("用户不存在")
        if (!BCrypt.checkpw(request.password, user.password)) {
            throw UnauthorizedException("用户名或密码错误")
        }
        return getJwtToken(user)
    }

    @PostMapping("refresh/{token}")
    fun refreshToken(@PathVariable("token") refreshToken: String): JwtToken {
        val token = refreshTokenService.get(refreshToken) ?: throw UnauthorizedException("refresh_token无效")
        if (Date().time < token.expired.time) {
            val user = userService.get(token.userId) ?: throw UnauthorizedException("user not exists.")
            return getJwtToken(user)
        } else {
            throw UnauthorizedException("refresh_token过期")
        }
    }

    @GetMapping(
        path = ["currentUser"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getCurrentUser(): User {
        return currentUserContext.currentUser()
    }

    @GetMapping("logout")
    fun logout() {
        tokenStoreService.delete(getCurrentUser().id.toString())
    }

    private fun getJwtToken(user: User): JwtToken {
        val jwtUser = JwtUser(
            userId = user.id.toString(),
            username = user.username,
        )
        val token = jwtUser.createToken()
        val refreshToken = generateRefreshToken()
        refreshTokenService.jpaDelete.where(QRefreshToken.refreshToken1.userId.eq(user.id))
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, REFRESH_TOKEN_EXPIRATION)
        refreshTokenService.insert(
            RefreshToken(
                refreshToken = refreshToken,
                userId = user.id,
                expired = calendar.time
            )
        )
        tokenStoreService.set(user.id.toString(), token)

        return JwtToken(
            accessToken = token,
            refreshToken = refreshToken,
            expires = TOKEN_EXPIRATION
        )
    }

    data class LoginRequest(
        @field:NotBlank(message = "username is required.")
        var username: String = "",
        @field:NotBlank(message = "password is required.")
        var password: String = ""
    )

    data class ChangePasswordRequest(
        @field:NotBlank(message = "old password is required.")
        var oldPassword: String = "",
        @field:NotBlank(message = "new password is required.")
        @field:Length(min = 6, max = 32, message = "password length 6~32 bit")
        var newPassword: String = ""
    )
}