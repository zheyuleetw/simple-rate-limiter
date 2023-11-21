package com.example.simpleratelimiter.config

import com.example.simpleratelimiter.algo.RateLimitAlgoBucket
import com.example.simpleratelimiter.algo.RateLimitTimeUnit
import com.example.simpleratelimiter.algo.Simple
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RateLimitConfig {
    @Bean
    fun registerAlgo(rateLimitAlgoBucket: RateLimitAlgoBucket): RateLimitAlgoBucket {
        rateLimitAlgoBucket.registerAlgo(
            Simple(
                unit = RateLimitTimeUnit.MINUTE,
                quota = 10,
                size = 1
            )
        )
        return rateLimitAlgoBucket
    }
}