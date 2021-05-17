package com.github.xsi640.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.xml.bind.DatatypeConverter

const val TOKEN_EXPIRATION = 12 * 60 * 60L
const val REFRESH_TOKEN_EXPIRATION = 30 * 24 * 60 * 60
const val JWT_SECRET_KEY = "QjckXfL8VabV1Z11RwTQ2Uz7aGsGV5PKj3ivhfS1LOM="
const val TOKEN_HEADER = "Authorization"
const val TOKEN_PARAMETER_NAME = "token"
const val REQUEST_USER_KEY = "CURRENT_USER"
const val TOKEN_PREFIX = "Bearer "
const val TOKEN_TYPE = "JWT"
val AUTH_WHITELIST = arrayOf(
    "/api/auth/login",
    "/api/auth/token/refresh",
)
const val FILTER_ALL = "/api/*"
const val CACHE_PREFIX = "XXX_TOKEN_"

val API_KEY_SECRET_BYTES = DatatypeConverter.parseBase64Binary(JWT_SECRET_KEY)
val SECRET_KEY = Keys.hmacShaKeyFor(API_KEY_SECRET_BYTES)

class JwtUser(
    val userId: String,
    val username: String,
    val permission: List<String>? = null
)

fun String.toClaims(): Claims {
    return Jwts.parserBuilder()
        .setSigningKey(SECRET_KEY)
        .build()
        .parseClaimsJws(this)
        .body
}

fun String.toJwtUser(): JwtUser {
    val claims = this.toClaims()
    val username = claims.subject
    val userId = claims.id
    return JwtUser(userId, username, listOf())
}

fun JwtUser.createToken(): String {
    val createdDate = Date()
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.SECOND, (TOKEN_EXPIRATION + 60).toInt())
    val expirationDate = calendar.time
    return Jwts.builder()
        .setHeaderParam("type", TOKEN_TYPE)
        .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
        .setId(this.userId)
        .setIssuer("xxx-mananger")
        .setIssuedAt(createdDate)
        .setSubject(this.username)
        .setExpiration(expirationDate)
        .compact()
}

fun generateRefreshToken(): String {
    val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    val data = key.encoded
    return DatatypeConverter.printBase64Binary(data)
}

fun String.removePrefix(): String {
    var newToken = this
    while (newToken.startsWith(TOKEN_PREFIX)) {
        newToken = newToken.substring(TOKEN_PREFIX.length).trim()
    }
    return newToken
}