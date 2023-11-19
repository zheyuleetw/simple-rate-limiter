package com.example.simpleratelimiter.model

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "rate-limit")
@Component
data class RateLimitProperty(
    var algo: String?,
    var enable: Boolean?,
    var second: Int?,
    var quota: Int?
)
