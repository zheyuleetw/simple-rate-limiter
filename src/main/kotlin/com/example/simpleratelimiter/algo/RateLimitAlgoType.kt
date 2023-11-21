package com.example.simpleratelimiter.algo

enum class RateLimitAlgoType {
    SIMPLE;

    fun algoInstance(unit: RateLimitTimeUnit, size: Int, quota: Int): RateLimitAlgo {
        return when (this) {
            SIMPLE -> Simple(unit, size, quota)
        }
    }

}