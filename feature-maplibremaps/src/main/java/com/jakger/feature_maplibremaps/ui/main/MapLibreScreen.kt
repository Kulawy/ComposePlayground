package com.jakger.feature_maplibremaps.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.maplibre.compose.map.MaplibreMap

@Composable
fun MapLibreScreen(
    modifier: Modifier = Modifier,
    viewModel: MapLibreViewModel = hiltViewModel()
) {
    MaplibreMap(

    )
//    Column(
//        modifier = modifier.fillMaxSize().statusBarsPadding(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("OpenStreetMapsScreen")
//    }
}