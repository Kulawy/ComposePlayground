package com.jakger.ghcrange_mobile.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jakger.featuregooglemaps.R

/***
 * Google Health Connect Range Start Screen
 */
@Composable
fun GhcRangeScreen(
    modifier: Modifier = Modifier,
    viewModel: GhcRangeViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("GoogleMapsMainScreen")
    }
}