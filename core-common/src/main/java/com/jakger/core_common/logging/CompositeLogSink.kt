package com.jakger.core_common.logging

/** Sends one already-constructed message to several outputs. */
class CompositeLogSink(vararg sinks: LogSink) : LogSink {
    private val outputs = sinks.copyOf()

    override fun write(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        for (output in outputs) {
            output.write(level, tag, message, throwable)
        }
    }
}
