package com.example.simpleratelimiter

import com.example.simpleratelimiter.algo.RateLimitTimeUnit
import com.example.simpleratelimiter.algo.Simple
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.test.annotation.DirtiesContext


class SimpleRateLimiterTest : RateLimiterTest() {

    @Test
    @DirtiesContext
    fun givenNonConcurrentRequestsThenReturnSuccessAndTooManyRequests() {

        val algo = Simple(RateLimitTimeUnit.MINUTE, 1, 10)
        rateLimitAlgoBucket.registerAlgo(algo)

        //success
        repeat(algo.quota) {
            getResourceAndVerifySuccess(MOCK_IP_1)
            getResourceAndVerifySuccess(MOCK_IP_2)
        }

        //too many request
        getResourceAndVerifyTooManyRequest(MOCK_IP_1)
        getResourceAndVerifyTooManyRequest(MOCK_IP_2)

        //next window success
        runBlocking { delay(algo.windowSizeMillis()) }
        getResourceAndVerifySuccess(MOCK_IP_1)
        getResourceAndVerifySuccess(MOCK_IP_2)

    }

    @Test
    @DirtiesContext
    fun givenConcurrentRequestsThenReturnSuccessAndTooManyRequests() {

        val algo = Simple(RateLimitTimeUnit.MINUTE, 1, 10)
        rateLimitAlgoBucket.registerAlgo(algo)

        // success
        runBlocking {
            (1..algo.quota).map {
                async {
                    getResourceAndVerifySuccess(MOCK_IP_1)
                    getResourceAndVerifySuccess(MOCK_IP_2)
                }
            }
        }

        // too many request
        getResourceAndVerifyTooManyRequest(MOCK_IP_1)
        getResourceAndVerifyTooManyRequest(MOCK_IP_2)

    }

    @Test
    @DirtiesContext
    fun givenZeroOrNegativeQuotaThenReturnTooManyRequest() {

        val algo1 = Simple(RateLimitTimeUnit.MINUTE, 1, 0)
        rateLimitAlgoBucket.registerAlgo(algo1)

        // too many request
        getResourceAndVerifyTooManyRequest(MOCK_IP_1)


        val algo2 = Simple(RateLimitTimeUnit.MINUTE, 1, -1)
        rateLimitAlgoBucket.registerAlgo(algo2)

        // too many request
        getResourceAndVerifyTooManyRequest(MOCK_IP_1)

    }

    @Test
    @DirtiesContext
    fun givenZeroOrNegativeSizeThenReturnSuccess() {

        val algo1 = Simple(RateLimitTimeUnit.MINUTE, -1, 10)
        rateLimitAlgoBucket.registerAlgo(algo1)

        // success
        repeat(algo1.quota) { getResourceAndVerifySuccess(MOCK_IP_1) }

        // success
        getResourceAndVerifySuccess(MOCK_IP_1)


        val algo2 = Simple(RateLimitTimeUnit.MINUTE, 0, 10)
        rateLimitAlgoBucket.registerAlgo(algo2)

        // success
        repeat(algo2.quota) { getResourceAndVerifySuccess(MOCK_IP_1) }

        // success
        getResourceAndVerifySuccess(MOCK_IP_1)
    }

}