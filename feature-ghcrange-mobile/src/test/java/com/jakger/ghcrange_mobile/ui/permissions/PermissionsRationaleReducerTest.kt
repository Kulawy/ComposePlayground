package com.jakger.ghcrange_mobile.ui.permissions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PermissionsRationaleReducerTest {
    private val reducer = PermissionsRationaleReducer()

    @Test
    fun `continue accepts the explanation without granting any health permission`() {
        val (state, effect) = reducer(
            PermissionsRationaleUiEvent.Continue,
            PermissionsRationaleUiState(isPermissionRequest = true),
        )

        assertTrue(state.isClosing)
        assertEquals(PermissionsRationaleUiSideEffect.Finish(true), effect)
    }

    @Test
    fun `declining or going back never continues to the system request`() {
        for (event in listOf(PermissionsRationaleUiEvent.NotNow, PermissionsRationaleUiEvent.Back)) {
            val (state, effect) = reducer(event, PermissionsRationaleUiState(isPermissionRequest = true))
            assertTrue(state.isClosing)
            assertEquals(PermissionsRationaleUiSideEffect.Finish(false), effect)
        }
    }

    @Test
    fun `external privacy entry cannot accept a permission request`() {
        val initial = PermissionsRationaleUiState(isPermissionRequest = false)
        assertEquals(initial to PermissionsRationaleUiSideEffect.None, reducer(PermissionsRationaleUiEvent.Continue, initial))
        assertEquals(
            PermissionsRationaleUiSideEffect.Finish(false),
            reducer(PermissionsRationaleUiEvent.Back, initial).second,
        )
    }

    @Test
    fun `a second tap cannot replace the first decision`() {
        val accepted = reducer(
            PermissionsRationaleUiEvent.Continue,
            PermissionsRationaleUiState(isPermissionRequest = true),
        ).first

        assertEquals(accepted to PermissionsRationaleUiSideEffect.None, reducer(PermissionsRationaleUiEvent.NotNow, accepted))
    }
}
