package com.example.simpleratelimiter.algo

import com.example.simpleratelimiter.util.getRemoteIp
import org.springframework.web.server.ServerWebExchange
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class Simple(
    val unit: RateLimitTimeUnit,
    val size: Int,
    val quota: Int
) : RateLimitAlgo {
    private data class Counter(var lastTime: LocalDateTime, var count: AtomicLong)

    private val counter = ConcurrentHashMap<String, Counter>()
    override fun isAvailable(exchange: ServerWebExchange): Boolean {
        return exchange.getRemoteIp()?.let {
            getCounter(it).getAndIncrement() < quota
        } ?: false
    }

    override fun type(): RateLimitAlgoType = RateLimitAlgoType.SIMPLE

    fun windowSizeMillis(): Long {
        val size = size.toLong()
        return when (unit) {
            RateLimitTimeUnit.SECOND -> 1 * size
            RateLimitTimeUnit.MINUTE -> 60 * size
            RateLimitTimeUnit.HOUR -> 60 * 60 * size
            RateLimitTimeUnit.DAY -> 60 * 60 * 24 * size
        } * 1000
    }

    private fun getCounter(ip: String): AtomicLong {
        val now = LocalDateTime.now()
        counter.putIfAbsent(ip, Counter(now, AtomicLong(0)))
        val counterByIp = counter[ip]!!
        synchronized(counterByIp) {
            return if (isTimeWindowExpired(counterByIp.lastTime, now)) {
                AtomicLong(0).also {
                    counterByIp.lastTime = now
                    counterByIp.count = it
                }
            } else {
                counter[ip]!!.count
            }
        }
    }

    private fun isTimeWindowExpired(last: LocalDateTime, now: LocalDateTime): Boolean {
        val duration = Duration.between(last, now)
        return when (unit) {
            RateLimitTimeUnit.SECOND -> duration.seconds >= size
            RateLimitTimeUnit.MINUTE -> duration.toMinutes() >= size
            RateLimitTimeUnit.HOUR -> duration.toHours() >= size
            RateLimitTimeUnit.DAY -> duration.toDays() >= size
        }
    }

}