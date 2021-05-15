package com.github.xsi640.auth.controller

import com.github.xsi640.auth.UnauthorizedException
import com.github.xsi640.auth.User
import com.github.xsi640.auth.UserRepository
import com.github.xsi640.auth.UserService
import com.github.xsi640.core.BaseService
import com.github.xsi640.core.NotFoundException
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {
    @Autowired
    private lateinit var userService: UserService

    @PostMapping("login")
    fun login(@Valid @RequestBody request: LoginRequest): User {
        val user = userService.findByUsername(request.username) ?: throw NotFoundException("user not exists.")
        if (!BCrypt.checkpw(request.password, user.password)) {
            throw UnauthorizedException("password error.")
        }
        return user
    }

    data class LoginRequest(
        @field:NotBlank(message = "username is required.")
        var username: String = "",
        @field:NotBlank(message = "password is required.")
        var password: String = ""
    )
}