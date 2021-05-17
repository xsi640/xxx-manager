package com.github.xsi640.auth.controller

import com.github.xsi640.auth.repository.User
import com.github.xsi640.auth.repository.UserService
import com.github.xsi640.core.*
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("/api/v1/user")
class UserController {
    @Autowired
    private lateinit var userService: BaseService<User, Long>

    @GetMapping
    fun list(
        @RequestParam(required = false, defaultValue = "1") page: Long,
        @RequestParam(required = false, defaultValue = "50") size: Long,
        @RequestParam(required = false, defaultValue = "id") sort: String,
        @RequestParam(required = false, defaultValue = "DESC") order: OrderMode
    ): Response<Paged<User>> {
        return userService.list(page, size, sort, order)
    }

    @GetMapping("{id}")
    fun get(@PathVariable("id") id: Long): User {
        return userService.get(id) ?: throw NotFoundException("The user not found.")
    }

    @PostMapping
    fun insert(@Valid @RequestBody request: UserRequest): User {
        return userService.insert(
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
        return userService.update(user, id)
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