package com.github.xsi640.auth.repository

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.xsi640.core.AuditableEntity
import com.github.xsi640.core.BaseRepository
import com.github.xsi640.core.BaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.persistence.*

@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @Column(nullable = false)
    var username: String,
    @Column(nullable = false, length = 255)
    @JsonIgnore
    var password: String,
    @Column(nullable = false, length = 255)
    var displayName: String
) : AuditableEntity()

@Repository
interface UserRepository : BaseRepository<User, Long> {
    fun findByUsername(username: String): User?
}

@Service
class UserService : BaseService<User, Long>() {

    @Autowired
    private lateinit var userRepository: UserRepository

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }
}