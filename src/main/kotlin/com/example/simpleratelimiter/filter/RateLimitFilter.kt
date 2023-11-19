package com.example.simpleratelimiter.filter

import com.example.simpleratelimiter.RateLimiter
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class RateLimitFilter(
    private val rateLimiter: RateLimiter
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return if (rateLimiter.isAvailable(exchange)) {
            chain.filter(exchange)
        } else {
            exchange.response.statusCode = HttpStatusCode.valueOf(429)
            Mono.empty()
        }
    }
}