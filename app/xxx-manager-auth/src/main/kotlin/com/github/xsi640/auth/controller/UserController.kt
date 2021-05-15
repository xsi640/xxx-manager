package com.github.xsi640.auth.controller

import com.github.xsi640.auth.User
import com.github.xsi640.auth.UserService
import com.github.xsi640.core.NotFoundException
import com.github.xsi640.core.OrderMode
import com.github.xsi640.core.Paged
import com.github.xsi640.core.Response
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("/api/v1/user")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @GetMapping
    fun list(
        @RequestParam(required = false, defaultValue = "1") page: Int,
        @RequestParam(required = false, defaultValue = "50") size: Int,
        @RequestParam(required = false, defaultValue = "id") sort: String,
        @RequestParam(required = false, defaultValue = "DESC") order: OrderMode
    ): Response<Paged<User>> {
        return Paged.of(userService.page(page, size, order, sort))
    }

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: Long): User {
        return userService.get(id) ?: throw NotFoundException("The user not found.")
    }

    @PostMapping
    fun insert(@Valid @RequestBody request: UserRequest): User {
        return userService.save(
            User(
                id = 0L,
                username = request.username,
                password = BCrypt.hashpw(request.password, BCrypt.gensalt()),
                displayName = request.displayName
            )
        )
    }

    @PutMapping("{id}")
    fun update(@PathVariable("id") id: Long, @Valid @RequestBody request: UpdateUserRequest): User {
        val user = userService.get(id) ?: throw NotFoundException("The user not found.")
        user.username = request.username
        user.displayName = request.displayName
        return userService.save(user)
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: Long) {
        userService.delete(id)
    }

    data class UserRequest(
        @field:NotBlank
        var username: String,
        @field:NotBlank
        var password: String,
        @field:NotBlank
        var displayName: String
    )

    data class UpdateUserRequest(
        @field:NotBlank
        var username: String,
        @field:NotBlank
        var displayName: String
    )
}