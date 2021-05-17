package com.github.xsi640.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class JwtToken(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("expires_in")
    val expires: Long = 0
)