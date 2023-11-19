package com.example.simpleratelimiter.util

import org.springframework.web.server.ServerWebExchange

fun ServerWebExchange.getRemoteIp(): String? {
    val request = request
    val headers = request.headers
    val remoteAddress = request.remoteAddress?.address?.hostAddress
    val xForwardedForHeader = headers.getFirst("X-Forwarded-For")

    if (!xForwardedForHeader.isNullOrBlank()) {
        // TODO check forwarded real remote IP
        val xForwardedForAddresses = xForwardedForHeader.split(",").map { it.trim() }
        return xForwardedForAddresses.last()
    }
    return remoteAddress
}