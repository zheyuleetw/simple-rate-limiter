package com.example.simpleratelimiter.algo

import com.example.simpleratelimiter.util.getRemoteIp
import org.springframework.web.server.ServerWebExchange
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.atomic.AtomicLong

class FixedWindowCounter(
    private val second: Int,
    private val quota: Int
) : RateLimitAlgo {

    private val window = ConcurrentHashMap<String?, ConcurrentMap<String, AtomicLong>>()

    override fun isAvailable(exchange: ServerWebExchange): Boolean {
        return insert(exchange.getRemoteIp())
    }

    override fun type(): RateLimitAlgoType = RateLimitAlgoType.FIXED_WINDOW_COUNTER

    private fun insert(ip: String?): Boolean {
        // TODO
        return true
    }

    private fun getWindow(ip: String?): AtomicLong {

        // TODO
        if (!window.contains(ip)) {
            window.putIfAbsent(ip, ConcurrentHashMap())
        }
        return AtomicLong()

    }
}