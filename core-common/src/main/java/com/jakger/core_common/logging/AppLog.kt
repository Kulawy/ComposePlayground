package com.jakger.core_common.logging

/** Application-wide logging. Disabled until configured by the application. */
object AppLog {
    @PublishedApi
    @Volatile
    internal var configuration = LogConfiguration()
        private set

    fun configure(minimumLevel: LogLevel, sink: LogSink) {
        configuration = LogConfiguration(minimumLevel, sink)
    }

    fun disable() {
        configuration = LogConfiguration()
    }

    inline fun v(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
        emit(LogLevel.VERBOSE, TagMode.CLASS, throwable, tag, message)

    inline fun d(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
        emit(LogLevel.DEBUG, TagMode.CLASS, throwable, tag, message)

    inline fun i(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
        emit(LogLevel.INFO, TagMode.CLASS, throwable, tag, message)

    inline fun w(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
        emit(LogLevel.WARN, TagMode.CLASS, throwable, tag, message)

    inline fun e(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
        emit(LogLevel.ERROR, TagMode.CLASS, throwable, tag, message)

    @PublishedApi
    internal inline fun emit(
        level: LogLevel,
        mode: TagMode,
        throwable: Throwable?,
        tag: String?,
        message: () -> String,
    ) {
        // Keep filtering and delivery on the same configuration snapshot.
        val config = configuration
        if (level == LogLevel.OFF || level.priority < config.minimumLevel.priority) return
        val sink = config.sink ?: return

        val resolvedTag = tag ?: CallerTags.resolve(mode)
        val resolvedMessage = message()
        sink.write(level, resolvedTag, resolvedMessage, throwable)
    }
}
