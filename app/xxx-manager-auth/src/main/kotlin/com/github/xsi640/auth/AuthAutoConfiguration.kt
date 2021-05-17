package com.github.xsi640.auth

import com.github.xsi640.auth.repository.User
import com.github.xsi640.auth.repository.UserRepository
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@EntityScan("com.github.xsi640.auth")
@ComponentScan("com.github.xsi640.auth")
class AuthAutoConfiguration : CommandLineRunner {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun run(vararg args: String?) {
        if (userRepository.count() == 0L) {
            val user = User(
                id = 1,
                username = "admin",
                password = BCrypt.hashpw("admin123", BCrypt.gensalt()),
                displayName = "超级管理员"
            )
            userRepository.save(user)
        }
    }
}