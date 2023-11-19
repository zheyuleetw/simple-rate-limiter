package com.example.simpleratelimiter.algo

import org.springframework.web.server.ServerWebExchange

class SlideWindowCounter(
    private val second: Int,
    private val quota: Int
) : RateLimitAlgo {
    override fun isAvailable(exchange: ServerWebExchange): Boolean {
        TODO("Not yet implemented")
    }

    override fun type(): RateLimitAlgoType = RateLimitAlgoType.SLIDE_WINDOW_COUNTER
}