package com.jakger.core_designsystem.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun getStatusBarHeight() : Dp = with( LocalDensity.current) {
    WindowInsets.statusBars.getTop(this).toDp()
}