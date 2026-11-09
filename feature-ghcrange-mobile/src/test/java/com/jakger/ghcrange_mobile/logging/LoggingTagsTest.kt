package com.jakger.ghcrange_mobile.logging

import com.jakger.core_common.logging.AppLog
import com.jakger.core_common.logging.ComposeLog
import com.jakger.core_common.logging.LogLevel
import com.jakger.core_common.logging.LogSink
import java.util.Collections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/** Exercises Kotlin call-site forms used by screens, without requiring Android or rendering UI. */
class LoggingTagsTest {
    private val tags = Collections.synchronizedList(mutableListOf<String>())

    @Before
    fun setUp() {
        AppLog.configure(LogLevel.DEBUG, LogSink { _, tag, _, _ -> tags += tag })
    }

    @After
    fun tearDown() {
        AppLog.disable()
    }

    @Test
    fun `nested feature classes use their own class name`() {
        FeatureClass().log()
        assertEquals(listOf("FeatureClass"), tags.toList())
    }

    @Test
    fun `a feature coroutine keeps the owning class tag`() = runBlocking {
        FeatureClass().logFromCoroutine()
        assertEquals(listOf("FeatureClass"), tags.toList())
    }

    @Test
    fun `function mode uses the enclosing function name for callbacks`() {
        GhcRangeScreen().invoke()
        assertEquals(listOf("GhcRangeScreen"), tags.toList())
    }

    @Test
    fun `function mode resolves the enclosing suspend function`() = runBlocking {
        GhcRangeScreenEffect()
        assertEquals(listOf("GhcRangeScreenEffect"), tags.toList())
    }

    @Test
    fun `class tags are not truncated to the legacy Android limit`() {
        FeatureWithANameLongerThanTwentyThreeCharacters().log()
        assertEquals(listOf("FeatureWithANameLongerThanTwentyThreeCharacters"), tags.toList())
    }

    private fun GhcRangeScreen(): () -> Unit = {
        ComposeLog.d { "Click received" }
    }

    private suspend fun GhcRangeScreenEffect() {
        withContext(Dispatchers.Default) {
            ComposeLog.i { "Effect started" }
        }
    }

    private class FeatureClass {
        fun log() {
            AppLog.d { "Feature event" }
        }

        suspend fun logFromCoroutine() {
            withContext(Dispatchers.Default) {
                AppLog.d { "Feature coroutine" }
            }
        }
    }

    private class FeatureWithANameLongerThanTwentyThreeCharacters {
        fun log() {
            AppLog.d { "Long class name" }
        }
    }
}
