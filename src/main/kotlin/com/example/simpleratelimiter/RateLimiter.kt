package com.example.simpleratelimiter

import com.example.simpleratelimiter.algo.RateLimitAlgo
import com.example.simpleratelimiter.algo.RateLimitAlgoBucket
import com.example.simpleratelimiter.algo.RateLimitAlgoType
import com.example.simpleratelimiter.algo.RateLimitTimeUnit
import com.example.simpleratelimiter.model.RateLimitProperty
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange

@Component
class RateLimiter(
    private val rateLimitProperty: RateLimitProperty,
    private val rateLimitAlgoBucket: RateLimitAlgoBucket
) {

    companion object {
        const val DEFAULT_SIZE = 60
        const val DEFAULT_QUOTA = 10
    }

    private var algoInstance: RateLimitAlgo? = null
    private var init: Boolean = false

    fun isAvailable(exchange: ServerWebExchange): Boolean {
        return when (rateLimitProperty.enable) {
            false -> true
            else -> {
                setAlgo()
                algoInstance?.isAvailable(exchange) ?: throw Exception("algo of rate limiter is null")
            }
        }
    }

    fun getAlgoInstance() = algoInstance

    private fun setAlgo() {
        if (init) return
        // algo setting priority application.properties > rateLimitAlgoBucket
        rateLimitAlgoBucket.algo?.let { algoInstance = it }
        rateLimitProperty.algo?.let {
            algoInstance = it.algoInstance(
                unit = rateLimitProperty.unit ?: RateLimitTimeUnit.SECOND,
                size = rateLimitProperty.size ?: DEFAULT_SIZE,
                quota = rateLimitProperty.quota ?: DEFAULT_QUOTA
            )
        }
        // default
        algoInstance = algoInstance ?: RateLimitAlgoType.SIMPLE.algoInstance(
            RateLimitTimeUnit.MINUTE, 1, 10
        )

        init = true
    }

}
