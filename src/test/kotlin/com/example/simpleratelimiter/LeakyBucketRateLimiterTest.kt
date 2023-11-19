package com.example.simpleratelimiter

import com.example.simpleratelimiter.algo.RateLimitAlgoType
import org.junit.jupiter.api.Test
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource


class LeakyBucketRateLimiterTest : RateLimiterTest() {

    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.add("rate-limit.algo") { RateLimitAlgoType.LEAKY_BUCKET.name }
            registry.add("rate-limit.second") { "60" }
            registry.add("rate-limit.quota") { "10" }
        }
    }

    @Test
    fun testLimitWithTokenBucket() {
        val quota = rateLimitProperty.quota!!
        val second = rateLimitProperty.second!!
        verifyType(RateLimitAlgoType.LEAKY_BUCKET)
        repeat(quota) { getResourceAndVerifySuccess(mockIp1) }
        repeat(quota) { getResourceAndVerifySuccess(mockIp2) }
        Thread.sleep(second.toLong() * 1000)
        repeat(quota) { getResourceAndVerifySuccess(mockIp1) }
        getResourceAndVerifyTooManyRequest(mockIp1)
    }

}