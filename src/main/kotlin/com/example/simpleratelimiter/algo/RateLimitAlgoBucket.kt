package com.example.simpleratelimiter.algo

import org.springframework.stereotype.Component

@Component
data class RateLimitAlgoBucket(
    var algo: RateLimitAlgo? = null
) {
    fun registerAlgo(algo: RateLimitAlgo) {
        this.algo = algo
    }
}