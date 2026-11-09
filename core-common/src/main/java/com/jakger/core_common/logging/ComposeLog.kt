package com.jakger.core_common.logging

/**
 * Requests function-name tags, including from onClick and LaunchedEffect callbacks.
 * This is ordinary Kotlin code and has no dependency on Compose.
 */
object ComposeLog {
    inline fun v(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
        AppLog.emit(LogLevel.VERBOSE, TagMode.FUNCTION, throwable, tag, message)

    inline fun d(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
        AppLog.emit(LogLevel.DEBUG, TagMode.FUNCTION, throwable, tag, message)

    inline fun i(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
        AppLog.emit(LogLevel.INFO, TagMode.FUNCTION, throwable, tag, message)

    inline fun w(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
        AppLog.emit(LogLevel.WARN, TagMode.FUNCTION, throwable, tag, message)

    inline fun e(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
        AppLog.emit(LogLevel.ERROR, TagMode.FUNCTION, throwable, tag, message)
}
