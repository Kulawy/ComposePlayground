package com.jakger.composeplaygroudmobile.logging

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LogChunkTest {
    @Test
    fun `short and empty messages are delivered once`() {
        for (text in listOf("", "Short message", "x".repeat(1_000))) {
            assertEquals(listOf(text), chunks(text))
        }
    }

    @Test
    fun `long messages retain every character and line break`() {
        val text = "A long line\nAnother line\n".repeat(200)
        val chunks = chunks(text)
        assertEquals(text, chunks.joinToString(""))
        assertTrue(chunks.size > 1)
        assertTrue(chunks.all { it.isNotEmpty() && it.length <= 1_000 })
    }

    @Test
    fun `a surrogate pair at a chunk boundary stays together`() {
        val text = "x".repeat(999) + "\uD83D\uDE00" + "y".repeat(1_200)
        val chunks = chunks(text)
        assertEquals(text, chunks.joinToString(""))
        for (chunk in chunks) {
            assertFalse(chunk.first().isLowSurrogate())
            assertFalse(chunk.last().isHighSurrogate())
        }
    }

    @Test
    fun `Unicode chunks remain within a conservative UTF-8 payload size`() {
        val text = "\u6F22\uD83D\uDE00".repeat(1_200)
        val chunks = chunks(text)
        assertEquals(text, chunks.joinToString(""))
        assertTrue(chunks.all { it.toByteArray(Charsets.UTF_8).size <= 3_000 })
    }

    private fun chunks(text: String): List<String> = buildList {
        forEachLogChunk(text) { add(it) }
    }
}
