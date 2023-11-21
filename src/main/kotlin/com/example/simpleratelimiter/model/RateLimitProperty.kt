package com.example.simpleratelimiter.model

import com.example.simpleratelimiter.algo.RateLimitAlgoType
import com.example.simpleratelimiter.algo.RateLimitTimeUnit
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "rate-limit")
@Component
data class RateLimitProperty(
    var algo: RateLimitAlgoType? = null,
    var enable: Boolean? = null,
    var unit: RateLimitTimeUnit? = null,
    var size: Int? = null,
    var quota: Int? = null
)
