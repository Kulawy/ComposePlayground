package com.jakger.core_common.logging

import java.util.Collections
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AppLogTest {
    private val entries = Collections.synchronizedList(mutableListOf<Entry>())
    private val sink = LogSink { level, tag, message, throwable ->
        entries += Entry(level, tag, message, throwable)
    }

    @Before
    fun setUp() {
        AppLog.disable()
    }

    @After
    fun tearDown() {
        AppLog.disable()
    }

    @Test
    fun `disabled logging never evaluates messages`() {
        var evaluated = false
        AppLog.e {
            evaluated = true
            "Disabled"
        }
        assertFalse(evaluated)
        assertTrue(entries.isEmpty())
    }

    @Test
    fun `messages below the configured level stay lazy`() {
        AppLog.configure(LogLevel.INFO, sink)
        AppLog.v { error("VERBOSE should be filtered") }
        AppLog.d { error("DEBUG should be filtered") }
        AppLog.i { "Included" }
        assertEquals(listOf("Included"), entries.map { it.message })
    }

    @Test
    fun `all logging methods deliver the correct level`() {
        AppLog.configure(LogLevel.VERBOSE, sink)
        AppLog.v { "Verbose" }
        AppLog.d { "Debug" }
        AppLog.i { "Info" }
        AppLog.w { "Warning" }
        AppLog.e { "Error" }
        assertEquals(LogLevel.entries.filter { it != LogLevel.OFF }, entries.map { it.level })
    }

    @Test
    fun `OFF suppresses even errors with an installed sink`() {
        AppLog.configure(LogLevel.OFF, sink)
        AppLog.e { error("OFF should prevent message construction") }
        assertTrue(entries.isEmpty())
    }

    @Test
    fun `exception is delivered without losing its identity`() {
        AppLog.configure(LogLevel.DEBUG, sink)
        val failure = IllegalStateException("Unavailable")
        AppLog.e(failure) { "Loading failed" }
        assertSame(failure, entries.single().throwable)
        assertEquals("Loading failed", entries.single().message)
    }

    @Test
    fun `tag overrides affect only their own call`() {
        AppLog.configure(LogLevel.DEBUG, sink)
        AppLog.d(tag = "CustomTag") { "First" }
        AppLog.d { "Second" }
        assertEquals(listOf("CustomTag", "AppLogTest"), entries.map { it.tag })
    }

    @Test
    fun `multiple outputs share one message construction`() {
        val otherEntries = mutableListOf<Entry>()
        val otherSink = LogSink { level, tag, message, throwable ->
            otherEntries += Entry(level, tag, message, throwable)
        }
        AppLog.configure(LogLevel.DEBUG, CompositeLogSink(sink, otherSink))
        var evaluations = 0
        AppLog.d {
            evaluations++
            "Shared"
        }
        assertEquals(1, evaluations)
        assertEquals(entries.toList(), otherEntries)
        assertEquals(1, entries.size)
    }

    @Test
    fun `an entry uses the configuration captured before constructing its message`() {
        val otherEntries = mutableListOf<String>()
        val otherSink = LogSink { _, _, message, _ -> otherEntries += message }
        AppLog.configure(LogLevel.DEBUG, sink)
        AppLog.d {
            AppLog.configure(LogLevel.ERROR, otherSink)
            "Original output"
        }
        AppLog.d { error("Updated threshold should apply to the next entry") }
        AppLog.e { "Updated output" }
        assertEquals(listOf("Original output"), entries.map { it.message })
        assertEquals(listOf("Updated output"), otherEntries)
    }

    @Test
    fun `message construction failures propagate to the caller`() {
        AppLog.configure(LogLevel.DEBUG, sink)
        val failure = IllegalStateException("Message failure")
        val thrown = assertThrows(IllegalStateException::class.java) {
            AppLog.d { throw failure }
        }
        assertSame(failure, thrown)
        assertTrue(entries.isEmpty())
    }

    @Test
    fun `concurrent calls keep their own tags and messages`() {
        AppLog.configure(LogLevel.DEBUG, sink)
        val executor = Executors.newFixedThreadPool(2)
        val ready = CountDownLatch(2)
        val start = CountDownLatch(1)
        try {
            val futures = listOf("First", "Second").map { tag ->
                executor.submit {
                    ready.countDown()
                    check(start.await(5, TimeUnit.SECONDS))
                    repeat(100) {
                        AppLog.d(tag = tag) { tag }
                    }
                }
            }
            assertTrue(ready.await(5, TimeUnit.SECONDS))
            start.countDown()
            futures.forEach { it.get(5, TimeUnit.SECONDS) }
            assertEquals(200, entries.size)
            assertTrue(entries.all { it.tag == it.message })
        } finally {
            start.countDown()
            executor.shutdownNow()
        }
    }

    private data class Entry(
        val level: LogLevel,
        val tag: String,
        val message: String,
        val throwable: Throwable?,
    )
}
