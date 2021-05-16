package com.github.xsi640.auth.controller

import com.github.xsi640.auth.UnauthorizedException
import com.github.xsi640.auth.User
import com.github.xsi640.auth.UserService
import com.github.xsi640.core.NotFoundException
import org.hibernate.validator.constraints.Length
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Thread.sleep
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {
    @Autowired
    private lateinit var userService: UserService

    @PostMapping("login")
    fun login(@Valid @RequestBody request: LoginRequest): User {
        val user = userService.findByUsername(request.username) ?: throw NotFoundException("用户不存在")
        if (!BCrypt.checkpw(request.password, user.password)) {
            throw UnauthorizedException("用户名或密码错误")
        }
        return user
    }

    fun changePassword(@Valid @RequestBody request: ChangePasswordRequest): User {
        TODO()
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