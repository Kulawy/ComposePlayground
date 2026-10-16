package com.jakger.googlemaps.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun GoogleMapsMainScreen(
    modifier: Modifier = Modifier,
    viewModel: GoogleMapsMainViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier
    ) {

    }
}