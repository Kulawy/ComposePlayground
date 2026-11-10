package com.jakger.ghcrange_mobile.ui.main

import org.junit.Assert.assertEquals
import org.junit.Test

class GhcRangePermissionFlowTest {
    private val reducer = GhcRangeReducer()

    @Test
    fun `permission explanation precedes system request and fetching waits for actual grants`() {
        val (explanation, explainEffect) = reducer(GhcRangeUiEvent.HealthPermissionsRequired, GhcRangeUiState.Initial)
        assertEquals(GhcRangeUiState.AwaitingPermissionExplanation(), explanation)
        assertEquals(GhcRangeUiSideEffect.UiEffect.ExplainPermissions, explainEffect)

        val (request, requestEffect) = reducer(GhcRangeUiEvent.RequestPermission, explanation)
        assertEquals(GhcRangeUiState.AwaitingPermissionResult(), request)
        assertEquals(GhcRangeUiSideEffect.UiEffect.RequestPermissions, requestEffect)

        val (loading, fetchEffect) = reducer(GhcRangeUiEvent.HealthPermissionsGranted, request)
        assertEquals(GhcRangeUiState.Loading, loading)
        assertEquals(GhcRangeUiSideEffect.FetchHealthData, fetchEffect)
    }

    @Test
    fun `declining the explanation or system request stops without fetching data`() {
        for (state in listOf(GhcRangeUiState.AwaitingPermissionExplanation(), GhcRangeUiState.AwaitingPermissionResult())) {
            assertEquals(
                GhcRangeUiState.Error.PermissionsNotGranted to GhcRangeUiSideEffect.None,
                reducer(GhcRangeUiEvent.HealthPermissionsDenied, state),
            )
        }
    }

    @Test
    fun `permission error can recheck access or open settings`() {
        val denied = GhcRangeUiState.Error.PermissionsNotGranted
        assertEquals(
            GhcRangeUiState.Initial to GhcRangeUiSideEffect.CheckHealthDataAvailability,
            reducer(GhcRangeUiEvent.CheckHealthDataAvailability, denied),
        )
        assertEquals(
            GhcRangeUiState.AwaitingPermissionSettings() to GhcRangeUiSideEffect.UiEffect.OpenPermissionSettings,
            reducer(GhcRangeUiEvent.ManagePermissions, denied),
        )
    }

    @Test
    fun `already granted access skips the explanation`() {
        assertEquals(
            GhcRangeUiState.Loading to GhcRangeUiSideEffect.FetchHealthData,
            reducer(GhcRangeUiEvent.HealthPermissionsGranted, GhcRangeUiState.Initial),
        )
    }

    @Test
    fun `duplicate continue events do not open multiple system requests`() {
        assertEquals(
            GhcRangeUiState.AwaitingPermissionResult() to GhcRangeUiSideEffect.None,
            reducer(GhcRangeUiEvent.RequestPermission, GhcRangeUiState.AwaitingPermissionResult()),
        )
    }

    @Test
    fun `a read failure after permissions are granted leaves the loading state`() {
        assertEquals(
            GhcRangeUiState.Error.HealthDataNotAvailable to GhcRangeUiSideEffect.None,
            reducer(GhcRangeUiEvent.HealthDataUnsupported, GhcRangeUiState.Loading),
        )
    }

    @Test
    fun `restored explanation accepts Continue without opening the explanation again`() {
        val waiting = reducer(
            GhcRangeUiEvent.PermissionUiEffectLaunched(GhcRangeUiSideEffect.UiEffect.ExplainPermissions),
            GhcRangeUiState.AwaitingPermissionExplanation(),
        ).first
        val savedStage = GhcRangePermissionStage.from(waiting)!!.name
        val restored = GhcRangePermissionStage.restore(savedStage)!!

        assertEquals(null, restored.pendingEffect)
        val (state, effect) = reducer(GhcRangeUiEvent.RequestPermission, restored.uiState)
        assertEquals(GhcRangeUiState.AwaitingPermissionResult(), state)
        assertEquals(GhcRangeUiSideEffect.UiEffect.RequestPermissions, effect)
    }

    @Test
    fun `restoring a queued launch preserves it until it has actually launched`() {
        for (stage in listOf(
            GhcRangePermissionStage.EXPLANATION_PENDING,
            GhcRangePermissionStage.PERMISSIONS_PENDING,
            GhcRangePermissionStage.SETTINGS_PENDING,
        )) {
            val restored = GhcRangePermissionStage.restore(stage.name)!!
            assertEquals(stage.pendingEffect, restored.pendingEffect)

            val (launched, effect) = reducer(
                GhcRangeUiEvent.PermissionUiEffectLaunched(restored.pendingEffect!!),
                restored.uiState,
            )
            assertEquals(GhcRangeUiSideEffect.None, effect)
            val restoredAfterLaunch = GhcRangePermissionStage.restore(GhcRangePermissionStage.from(launched)!!.name)!!
            assertEquals(null, restoredAfterLaunch.pendingEffect)
        }
    }

    @Test
    fun `restored system request accepts a grant or denial without launching again`() {
        val restored = GhcRangePermissionStage.restore("PERMISSIONS_LAUNCHED")!!
        assertEquals(null, restored.pendingEffect)
        assertEquals(
            GhcRangeUiState.Loading to GhcRangeUiSideEffect.FetchHealthData,
            reducer(GhcRangeUiEvent.HealthPermissionsGranted, restored.uiState),
        )
        assertEquals(
            GhcRangeUiState.Error.PermissionsNotGranted to GhcRangeUiSideEffect.None,
            reducer(GhcRangeUiEvent.HealthPermissionsDenied, restored.uiState),
        )
    }

    @Test
    fun `restored settings waits for its result before rechecking availability`() {
        val restored = GhcRangePermissionStage.restore("SETTINGS_LAUNCHED")!!
        assertEquals(null, restored.pendingEffect)
        assertEquals(
            GhcRangeUiState.Initial to GhcRangeUiSideEffect.CheckHealthDataAvailability,
            reducer(GhcRangeUiEvent.PermissionSettingsClosed, restored.uiState),
        )
    }

    @Test
    fun `failed launches leave the waiting state with a recoverable error`() {
        for (stage in GhcRangePermissionStage.entries) {
            assertEquals(
                GhcRangeUiState.Error.HealthDataNotAvailable to GhcRangeUiSideEffect.None,
                reducer(GhcRangeUiEvent.PermissionUiLaunchFailed, stage.uiState),
            )
        }
    }

    @Test
    fun `an outdated launch acknowledgement cannot consume the current request`() {
        val current = GhcRangeUiState.AwaitingPermissionResult()
        assertEquals(
            current to GhcRangeUiSideEffect.None,
            reducer(
                GhcRangeUiEvent.PermissionUiEffectLaunched(GhcRangeUiSideEffect.UiEffect.ExplainPermissions),
                current,
            ),
        )
        assertEquals(GhcRangeUiSideEffect.UiEffect.RequestPermissions, GhcRangePermissionStage.from(current)!!.pendingEffect)
    }

    @Test
    fun `resuming recovers queued launches but never reopens in-flight activities`() {
        for (stage in GhcRangePermissionStage.entries) {
            assertEquals(
                stage.uiState to (stage.pendingEffect ?: GhcRangeUiSideEffect.None),
                reducer(GhcRangeUiEvent.ScreenResumed, stage.uiState),
            )
        }
    }
}
