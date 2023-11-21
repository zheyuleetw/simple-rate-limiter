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
                unit = RateLimitTimeUnit.SECOND,
                quota = -1,
                size = 10
            )
        )
        return rateLimitAlgoBucket
    }
}