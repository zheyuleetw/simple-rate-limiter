package com.example.simpleratelimiter

import com.example.simpleratelimiter.algo.RateLimitAlgo
import com.example.simpleratelimiter.algo.RateLimitAlgoType
import com.example.simpleratelimiter.model.RateLimitProperty
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange

@Component
class RateLimiter(
    private val rateLimitProperty: RateLimitProperty
) {

    private val defaultSecond = 60
    private val defaultQuota = 10
    private lateinit var algoInstance: RateLimitAlgo

    fun isAvailable(exchange: ServerWebExchange): Boolean {
        return when (rateLimitProperty.enable) {
            false -> true
            else -> {
                if (!::algoInstance.isInitialized) {
                    algoInstance = RateLimitAlgoType.fromStringOrDefault(rateLimitProperty.algo).algoInstance(
                        second = rateLimitProperty.second ?: defaultSecond,
                        quota = rateLimitProperty.quota ?: defaultQuota
                    )
                }
                algoInstance.isAvailable(exchange)
            }
        }
    }

    fun getAlgoInstance() = algoInstance

}
