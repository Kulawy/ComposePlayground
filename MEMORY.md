# Project memory

## Logging solution recap — 2026-11-09

Saved at the user's request for continuity across sessions.

### Architecture

- Shared Kotlin logging lives in `core-common/src/main/java/com/jakger/core_common/logging/`.
- `AppLog` provides automatic calling-class tags.
- `ComposeLog` requests function-name tags, including from callbacks and coroutines; it has no Compose dependency.
- Both expose `v`, `d`, `i`, `w`, and `e`, with optional exceptions and explicit tag overrides.
- Messages are lazy lambdas, evaluated only when logging is enabled and the configured minimum level permits the entry.
- `LogSink` defines the output interface; `CompositeLogSink` forwards an entry to multiple outputs.
- Android output lives in `app-mobile/src/main/java/com/jakger/composeplaygroudmobile/logging/AndroidLogSink.kt`.
- `AndroidLogSink` writes to Logcat, includes exception stack traces, and chunks long messages without splitting Unicode surrogate pairs.

### Configuration and usage

`ComposePlaygroundApp.onCreate()` configures `AppLog` with `LogLevel.DEBUG` and `AndroidLogSink` for debug builds, and disables logging for release builds.

```kotlin
AppLog.d { "Loading data" }
AppLog.i(tag = "Sync") { "Sync completed" }
AppLog.e(throwable = exception) { "Loading failed" }
ComposeLog.d { "Permission result received" }
```

The Health Connect screen, ViewModel, and availability use case already use the logger.

### Validation and recorded state

Tests exist for filtering, lazy evaluation, exception delivery, tag resolution, concurrency, multiple outputs, configuration snapshots, and Unicode message chunking. Feature tests exercise logging integration.

At the time of the recap, the logging implementation and integration changes were uncommitted. Tests were inspected but were not run during the recap. Recheck Git status and run relevant checks before reporting current validation or commit status.
