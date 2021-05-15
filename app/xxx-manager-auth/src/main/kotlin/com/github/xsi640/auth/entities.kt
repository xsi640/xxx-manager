package com.github.xsi640.auth

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.xsi640.core.BaseRepository
import org.springframework.stereotype.Repository
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
)

@Repository
interface UserRepository : BaseRepository<User, Long> {
    fun findByUsername(username: String): User?
}