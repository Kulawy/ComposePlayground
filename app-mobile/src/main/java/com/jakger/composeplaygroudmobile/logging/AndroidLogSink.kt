package com.jakger.composeplaygroudmobile.logging

import android.util.Log
import com.jakger.core_common.logging.LogLevel
import com.jakger.core_common.logging.LogSink

object AndroidLogSink : LogSink {
    override fun write(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        if (level == LogLevel.OFF) return
        val text = if (throwable == null) message else "$message\n${throwable.stackTraceToString()}"
        forEachLogChunk(text) { chunk ->
            Log.println(level.priority, tag, chunk)
        }
    }
}

private const val CHUNK_SIZE = 1_000

/** Conservative Unicode chunking, independent of Android so its boundaries can be unit tested. */
internal inline fun forEachLogChunk(text: String, write: (String) -> Unit) {
    if (text.length <= CHUNK_SIZE) {
        write(text)
        return
    }

    var start = 0
    while (start < text.length) {
        var end = minOf(start + CHUNK_SIZE, text.length)
        if (
            end < text.length &&
            text[end - 1].isHighSurrogate() &&
            text[end].isLowSurrogate()
        ) {
            end--
        }
        write(text.substring(start, end))
        start = end
    }
}
