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

## Health Connect permission flow — 2026-11-09

- The missing system permission screen was confirmed on the API 36 emulator by Logcat: `App should support rationale intent, finishing!`.
- Implemented `PermissionsRationaleActivity` in `feature-ghcrange-mobile/ui/permissions`, with its own state, events, reducer, side effects, and Hilt ViewModel using `core-arch` MVI contracts.
- The feature manifest declares the legacy rationale intent and the Android 14+ permission-usage alias. Both merge into the app manifest.
- The explanation uses the user's exact message: `There is need to health data usage permissions to app work.`
- The feature opens the activity through `PermissionsRationaleContract`. Continue accepts the explanation only; `GhcRangeScreen` then launches the actual Health Connect permission contract. Not now or Back returns cancellation.
- When opened from Health Connect's privacy-policy link, the activity displays data-use information with Back to permissions and never starts another permission request.
- `GhcRangeReducer` distinguishes explanation and system-result states; fetching starts only after permissions are granted. Denial offers retry, Health Connect settings, and back navigation.
- Validation at implementation time: debug APK built; all 16 feature unit tests passed, including 10 new permission-flow tests. On the API 36 emulator, verified Not now, retry, Continue, the real system prompt, privacy-policy navigation and return, system denial, and granting all five requested read permissions. The emulator ended with those five read permissions granted.
- Changes were left uncommitted. Recheck files and validation before describing future status.

### Permission flow fixes after static review

- Availability is checked before querying permissions. Provider/query failures return `HealthConnectAccess.Unavailable`; coroutine cancellation propagates.
- `GhcRangeViewModel` saves the permission stage in `SavedStateHandle`. Explanation, system permissions, and settings each distinguish queued launches from activities already launched.
- Restored in-flight activities wait for their activity results, without starting a new availability check or reopening the explanation. `ScreenResumed` recovers queued launches.
- Reducer updates and launch acknowledgements are synchronous on the main thread, so progress is saved before opening an external activity. Buffered launches are checked against the current state before delivery.
- UI launches are collected only while the screen lifecycle is `RESUMED`. Launch failures leave the waiting state with an error.
- Verification for these fixes: debug APK and domain/mobile regression-test sources compiled successfully; static diff checks passed. Tests were not executed, and no ADB, emulator, or screenshots were used for these fixes, following the user's restrictions. The earlier runtime checks above apply to the implementation before this hardening.
