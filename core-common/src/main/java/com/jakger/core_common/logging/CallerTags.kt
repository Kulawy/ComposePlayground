package com.jakger.core_common.logging

/** JVM call-site inference is best effort for inlined, generated, or obfuscated code. */
@PublishedApi
internal object CallerTags {
    private val internalClasses = setOf(
        AppLog::class.java.name,
        ComposeLog::class.java.name,
        CallerTags::class.java.name,
    )

    fun resolve(mode: TagMode): String {
        val caller = Throwable().stackTrace.firstOrNull {
            it.className !in internalClasses
        } ?: return "App"

        return when (mode) {
            TagMode.CLASS -> classTag(caller)
            TagMode.FUNCTION -> functionTag(caller)
        }
    }

    private fun classTag(frame: StackTraceElement): String {
        val original = frame.className.substringAfterLast('.').substringBefore("\$\$")
        var owner = removeNumericSuffixes(original)

        // Example: ViewModel$fetchData$1.invokeSuspend -> ViewModel.
        if (
            owner != original &&
            isLambdaMethod(frame.methodName) &&
            owner.substringAfterLast('$').firstOrNull()?.isLowerCase() == true
        ) {
            owner = owner.substringBeforeLast('$', owner)
        }

        return owner.removeSuffix("\$Companion").substringAfterLast('$').ifBlank { "App" }
    }

    private fun functionTag(frame: StackTraceElement): String {
        val method = frame.methodName.substringBefore('$').substringBefore('-')
        if (!isLambdaMethod(method)) return method.ifBlank { "Function" }

        // Example: ScreensKt$GhcRangeScreen$1.invokeSuspend -> GhcRangeScreen.
        val owner = removeNumericSuffixes(frame.className.substringAfterLast('.'))
        return owner.substringAfterLast('$', "").substringBefore('-')
            .takeUnless { it.isBlank() || it == "Companion" }
            ?: method
    }

    private fun removeNumericSuffixes(name: String): String {
        var result = name
        while ('$' in result) {
            val suffix = result.substringAfterLast('$')
            if (suffix.isEmpty() || !suffix.all { it.isDigit() }) break
            result = result.substringBeforeLast('$')
        }
        return result
    }

    private fun isLambdaMethod(method: String): Boolean =
        method == "invoke" || method == "invokeSuspend"
}
