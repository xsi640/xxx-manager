package com.github.xsi640.auth.repository

import com.github.xsi640.core.BaseRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Entity
data class RefreshToken(
    @Id
    @Column(name = "refresh_token", nullable = false)
    var refreshToken: String,
    @Column(nullable = false)
    var userId: Long,
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    var expired: Date
)

@Repository
interface RefreshTokenRepository : BaseRepository<RefreshToken, String>