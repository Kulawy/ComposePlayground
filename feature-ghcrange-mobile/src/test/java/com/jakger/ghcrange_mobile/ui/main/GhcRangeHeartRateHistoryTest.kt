package com.jakger.ghcrange_mobile.ui.main

import com.jakger.feature_ghcrange.model.HeartRateAggregatedStats
import com.jakger.feature_ghcrange.model.toUi
import org.junit.Assert.assertEquals
import org.junit.Test

class GhcRangeHeartRateHistoryTest {
    private val reducer = GhcRangeReducer()
    private val yesterday = HeartRateAggregatedStats(55, 70, 120, 20)
    private val lastYear = HeartRateAggregatedStats(45, 65, 180, 1200)
    private val yesterdayResting = HeartRateAggregatedStats(50, 60, 70, 2)
    private val lastYearResting = HeartRateAggregatedStats(40, 55, 85, 300)
    private val displayed = GhcRangeUiState.Display(
        aggregatedYesterdayHeartRate = yesterday.toUi(),
        aggregatedLastYearHeartRate = lastYear.toUi(),
        aggregatedYesterdayRestingHeartRate = yesterdayResting.toUi(),
        aggregatedLastYearRestingHeartRate = lastYearResting.toUi(),
    )

    @Test
    fun `all four heart rate summaries are displayed together in the flat display state`() {
        val (state, effect) = reducer(
            GhcRangeUiEvent.HeartRateFetched(yesterday, lastYear, yesterdayResting, lastYearResting),
            GhcRangeUiState.Loading,
        )
        assertEquals(displayed, state)
        assertEquals(GhcRangeUiSideEffect.None, effect)
    }

    @Test
    fun `any missing permission displays the full screen error`() {
        for (state in listOf(
            GhcRangeUiState.Initial,
            GhcRangeUiState.AwaitingPermissionResult(launchPending = false),
            GhcRangeUiState.Loading,
            displayed,
        )) {
            assertEquals(
                GhcRangeUiState.Error.PermissionsNotGranted to GhcRangeUiSideEffect.None,
                reducer(GhcRangeUiEvent.HealthPermissionsDenied, state),
            )
        }
    }

    @Test
    fun `a data failure replaces the entire screen`() {
        for (state in listOf(GhcRangeUiState.Loading, displayed)) {
            assertEquals(
                GhcRangeUiState.Error.HealthDataNotAvailable to GhcRangeUiSideEffect.None,
                reducer(GhcRangeUiEvent.HealthDataUnsupported, state),
            )
        }
    }

    @Test
    fun `resuming the summary rechecks permissions without asking for them`() {
        assertEquals(
            displayed to GhcRangeUiSideEffect.CheckHealthDataPermissions,
            reducer(GhcRangeUiEvent.ScreenResumed, displayed),
        )
    }

    @Test
    fun `summary cards cannot open permission requests or settings`() {
        for (event in listOf(GhcRangeUiEvent.RequestPermission, GhcRangeUiEvent.ManagePermissions)) {
            assertEquals(displayed to GhcRangeUiSideEffect.None, reducer(event, displayed))
        }
    }

    @Test
    fun `missing startup access opens the explanation before the system request`() {
        val (explanation, effect) = reducer(GhcRangeUiEvent.HealthPermissionsRequired, GhcRangeUiState.Initial)
        assertEquals(GhcRangeUiState.AwaitingPermissionExplanation(), explanation)
        assertEquals(GhcRangeUiSideEffect.UiEffect.ExplainPermissions, effect)
        assertEquals(
            GhcRangeUiState.AwaitingPermissionResult() to GhcRangeUiSideEffect.UiEffect.RequestPermissions,
            reducer(GhcRangeUiEvent.RequestPermission, explanation),
        )
    }
}
