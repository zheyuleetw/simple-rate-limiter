package com.example.simpleratelimiter.algo

import org.springframework.web.server.ServerWebExchange

interface RateLimitAlgo {
    fun isAvailable(exchange: ServerWebExchange): Boolean
    fun type(): RateLimitAlgoType
}