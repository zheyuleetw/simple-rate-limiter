package com.example.simpleratelimiter.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class ResourceController {

    @GetMapping("/api/resource")
    fun getResource(): Mono<String> {
        return Mono.just("Hello Simple Rate Limiter!")
    }

}