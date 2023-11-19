package com.example.simpleratelimiter.algo

import com.example.simpleratelimiter.util.getRemoteIp
import org.springframework.web.server.ServerWebExchange
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class TokenBucket(
    private val second: Int,
    private val quota: Int
) : RateLimitAlgo {

    private val bucket = ConcurrentHashMap<String?, AtomicInteger>()
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    init {
        validSetting()
        val averageRefillRate = if (second / quota > 0) {
            second / quota
        } else {
            1
        }
        scheduler.scheduleAtFixedRate(
            { refill() }, 0, averageRefillRate.toLong(), TimeUnit.SECONDS
        )
    }

    override fun isAvailable(exchange: ServerWebExchange): Boolean {
        val remoteIp = exchange.getRemoteIp()
        val theBucket = getBucket(remoteIp)
        return if (theBucket.get() > 0) {
            theBucket.decrementAndGet()
            true
        } else {
            false
        }
    }

    override fun type(): RateLimitAlgoType = RateLimitAlgoType.TOKEN_BUCKET

    private fun validSetting() {
        if (second <= 0 || quota <= 0) {
            throw IllegalArgumentException("Invalid rate limiter setting: second: $second, quota: $quota")
        }
    }

    private fun refill() {
        bucket.values.forEach {
            if (it.get() < quota) {
                if (second / quota > 0) {
                    it.incrementAndGet()
                } else {
                    it.set(quota)
                }
            }
        }
    }

    private fun getBucket(ip: String?): AtomicInteger {
        if (bucket[ip] == null) {
            bucket.putIfAbsent(ip, AtomicInteger(quota))
        }
        return bucket[ip]!!
    }
}