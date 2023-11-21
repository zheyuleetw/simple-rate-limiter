package com.example.simpleratelimiter

import com.example.simpleratelimiter.algo.RateLimitAlgoBucket
import com.example.simpleratelimiter.algo.RateLimitAlgoType
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class RateLimiterTest {

    @Autowired
    lateinit var client: WebTestClient

    @Autowired
    lateinit var rateLimitAlgoBucket: RateLimitAlgoBucket

    companion object {
        const val MOCK_IP_1 = "127.0.0.1"
        const val MOCK_IP_2 = "127.0.0.2"
    }

    protected fun verifyType(type: RateLimitAlgoType) {
        assertEquals(rateLimitAlgoBucket.algo?.type(), type)
    }

    protected fun getResourceAndVerifySuccess(ip: String) {
        runBlocking{
            delay(1000)
        }
        getResource(ip)
            .expectStatus().isOk
            .expectBody(String::class.java).isEqualTo("Hello Simple Rate Limiter!")
    }

    protected fun getResourceAndVerifyTooManyRequest(ip: String) {
        getResource(ip)
            .expectStatus().isEqualTo(HttpStatus.TOO_MANY_REQUESTS)
    }

    protected fun getResourceAndVerify500(ip: String) {
        getResource(ip)
            .expectStatus().is5xxServerError
    }

    private fun getResource(ip: String): WebTestClient.ResponseSpec {
        return client.get()
            .uri("/api/resource")
            .header("X-Forwarded-For", ip)
            .exchange()
    }
}