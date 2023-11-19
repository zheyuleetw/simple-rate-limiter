package com.example.simpleratelimiter.algo

enum class RateLimitAlgoType {
    TOKEN_BUCKET,
    LEAKY_BUCKET,
    FIXED_WINDOW_COUNTER,
    SLIDE_WINDOW_LOG,
    SLIDE_WINDOW_COUNTER;

    companion object {
        fun fromStringOrDefault(value: String?): RateLimitAlgoType {
            return when (value) {
                "TOKEN_BUCKET" -> TOKEN_BUCKET
                "LEAKY_BUCKET" -> LEAKY_BUCKET
                "FIXED_WINDOW_COUNTER" -> FIXED_WINDOW_COUNTER
                "SLIDE_WINDOW_LOG" -> SLIDE_WINDOW_LOG
                "SLIDE_WINDOW_COUNTER" -> SLIDE_WINDOW_COUNTER
                else -> TOKEN_BUCKET
            }
        }
    }

    fun algoInstance(second: Int, quota: Int): RateLimitAlgo {
        return when (this) {
            TOKEN_BUCKET -> TokenBucket(second, quota)
            LEAKY_BUCKET -> LeakyBucket(second, quota)
            FIXED_WINDOW_COUNTER -> FixedWindowCounter(second, quota)
            SLIDE_WINDOW_LOG -> SlideWindowLog(second, quota)
            SLIDE_WINDOW_COUNTER -> SlideWindowCounter(second, quota)
        }

    }

}