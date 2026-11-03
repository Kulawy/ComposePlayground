package com.jakger.feature_maplibremaps.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.visible
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jakger.feature_maplibremaps.R
import com.jakger.feature_maplibremaps.ui.utils.Dimens
import org.maplibre.compose.camera.CameraUpdate
import org.maplibre.compose.location.LocationTrackingEffect
import org.maplibre.compose.location.rememberDefaultHeadingProvider
import org.maplibre.compose.location.rememberDefaultLocationProvider
import org.maplibre.compose.location.rememberLocationState
import org.maplibre.compose.map.LocalMapState
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.rememberMapState
import org.maplibre.compose.material3.LocationIndicatorLayer
import org.maplibre.compose.overlay.MapOverlay
import org.maplibre.compose.overlay.include
import org.maplibre.compose.style.BaseStyle
import com.jakger.core_designsystem.R as DS

@Composable
fun MapLibreScreen(
    modifier: Modifier = Modifier,
    viewModel: MapLibreViewModel = hiltViewModel()
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    when (val screenState = state.value) {
        is MapLibreUiState.View -> MapLibreScreenContent(
            state = screenState,
            onEvent = { event -> viewModel.dispatchUiEvent(event) }
        )

        else -> {}
    }
}

@Composable
private fun MapLibreScreenContent(
    modifier: Modifier = Modifier,
    state: MapLibreUiState.View,
    onEvent: (MapLibreMapsUiEvent) -> Unit,
) {
    val location = rememberLocationState(
        provider = rememberDefaultLocationProvider(),
        headingProvider = rememberDefaultHeadingProvider()
    )

    val mapState =
        rememberMapState(
            baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty")
        ) {
            val mapState = checkNotNull(LocalMapState.current)
            LocationIndicatorLayer(
                id = "user",
                locationState = location
            )
            LocationTrackingEffect(locationState = location) {
                mapState.animateCamera(
                    CameraUpdate(center = currentLocation.position, zoom = state.zoom)
                )
            }
        }

    MaplibreMap(
//        baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty")
        state = mapState,
        overlay = {
            include(MapOverlay.None)

            IconButton(
                modifier = Modifier
                    .testTag("MapMenuIcon")
                    .padding(dimensionResource(DS.dimen.margin_16))
                    .size(Dimens.mapIcon)
                    .align(Alignment.BottomEnd),
                onClick = { onEvent(MapLibreMapsUiEvent.menuClick) },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu_64),
                    contentDescription = stringResource(R.string.zoom_in),
                    tint = Color.Unspecified,
                    modifier = modifier,
                )
            }

            Column(
                modifier = Modifier
                    .visible(state.menuExpanded)
                    .testTag("MapMenu")
                    .padding(dimensionResource(DS.dimen.margin_16))
                    .align(Alignment.CenterEnd)
            ) {
                IconButton(
                    modifier = Modifier.size(Dimens.mapIcon),
                    onClick = { onEvent(MapLibreMapsUiEvent.zoomInClick) },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_zoom_in_64),
                        contentDescription = stringResource(R.string.zoom_in),
                        tint = Color.Unspecified,
                        modifier = modifier,
                    )
                }

                IconButton(
                    modifier = Modifier.size(Dimens.mapIcon),
                    onClick = { onEvent(MapLibreMapsUiEvent.zoomOutClick) },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_zoom_out_64),
                        contentDescription = stringResource(R.string.zoom_out),
                        tint = Color.Unspecified,
                        modifier = modifier,
                    )
                }
            }
        }
    )
}

