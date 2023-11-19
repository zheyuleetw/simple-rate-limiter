package com.example.simpleratelimiter

import com.example.simpleratelimiter.algo.RateLimitAlgoType
import com.example.simpleratelimiter.model.RateLimitProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient


@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
class RateLimiterTest {

    @Autowired
    lateinit var client: WebTestClient

    @Autowired
    lateinit var rateLimitProperty: RateLimitProperty

    protected val mockIp1 = "127.0.0.1"
    protected val mockIp2 = "127.0.0.2"

    protected fun verifyType(type: RateLimitAlgoType) {
        assert(rateLimitProperty.algo == type.name)
    }

    protected fun getResourceAndVerifySuccess(ip: String) {
        client.get()
            .uri("/api/resource")
            .header("X-Forwarded-For", ip)
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java).isEqualTo("Hello Simple Rate Limiter!")
    }

    protected fun getResourceAndVerifyTooManyRequest(ip: String) {
        client.get()
            .uri("/api/resource")
            .header("X-Forwarded-For", ip)
            .exchange()
            .expectStatus().isEqualTo(429)
    }
}