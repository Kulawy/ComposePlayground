package com.jakger.core_common.logging

enum class LogLevel(val priority: Int) {
    VERBOSE(2),
    DEBUG(3),
    INFO(4),
    WARN(5),
    ERROR(6),
    OFF(Int.MAX_VALUE),
}

/** Receives messages on the calling thread. Implementations must support concurrent calls. */
fun interface LogSink {
    fun write(level: LogLevel, tag: String, message: String, throwable: Throwable?)
}

@PublishedApi
internal data class LogConfiguration(
    val minimumLevel: LogLevel = LogLevel.OFF,
    val sink: LogSink? = null,
)

@PublishedApi
internal enum class TagMode {
    CLASS,
    FUNCTION,
}
