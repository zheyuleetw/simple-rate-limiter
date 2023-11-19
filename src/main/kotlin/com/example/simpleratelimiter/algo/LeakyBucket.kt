package com.example.simpleratelimiter.algo

import com.example.simpleratelimiter.util.getRemoteIp
import org.springframework.web.server.ServerWebExchange
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

class LeakyBucket(
    private val second: Int,
    private val quota: Int
) : RateLimitAlgo {
    private val buckets = ConcurrentHashMap<String?, AtomicLong>()
    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

    init {
        validSetting()
        if (quota > 0) {
            val averageLeakingRate = second / quota
            scheduler.scheduleAtFixedRate(
                { leak() }, 0, averageLeakingRate.toLong(), TimeUnit.SECONDS
            )
        }
    }

    override fun isAvailable(exchange: ServerWebExchange): Boolean {
        val remoteIp = exchange.getRemoteIp()
        if (quota == 0) return false
        val bucket = getBucket(remoteIp)
        return if (bucket.get() >= quota) {
            false
        } else {
            bucket.incrementAndGet()
            true
        }
    }

    override fun type(): RateLimitAlgoType = RateLimitAlgoType.LEAKY_BUCKET

    private fun validSetting() {
        if (second <= 0) {
            throw IllegalArgumentException("Invalid rate limiter setting: second: $second")
        }
        if (quota < 0) {
            throw IllegalArgumentException("Invalid rate limiter setting: quota: $quota")
        }
    }

    private fun leak() {
        buckets.values.forEach {
            if (it.get() > 0) {
                it.decrementAndGet()
            }
        }
    }

    private fun getBucket(ip: String?): AtomicLong {
        if (!buckets.contains(ip)) {
            buckets.putIfAbsent(ip, AtomicLong(0))
        }
        return buckets[ip]!!
    }
}