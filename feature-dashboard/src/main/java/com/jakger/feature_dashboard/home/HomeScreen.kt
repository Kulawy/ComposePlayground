package com.jakger.feature_dashboard.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation3.runtime.NavKey
import com.jakger.core_arch.navi.AppNavigator
import com.jakger.core_designsystem.ui.theme.ComposePlaygroundTheme
import com.jakger.feature_dashboard.R
import com.jakger.feature_dashboard_api.DashboardNavigationFlow
import com.jakger.feature_googlemaps_api.GoogleMapsNavFlow
import com.jakger.feature_maplibremaps_api.MapLibreMapsNavigationFlow

@Composable
fun HomeScreen(
    navigator: AppNavigator
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color.DarkGray, Color.LightGray),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
            .statusBarsPadding()
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val items = listOf<kotlin.Pair<kotlin.String, NavKey>>(
            stringResource(R.string.buttons_title) to DashboardNavigationFlow.Buttons,
            stringResource(R.string.googlemaps_title) to GoogleMapsNavFlow.Main,
            stringResource(R.string.openstreetmaps_title) to MapLibreMapsNavigationFlow.Main
        )
        Text(
            text = stringResource(R.string.dashboard_title),
            style = MaterialTheme.typography.titleLarge
        )
        items.forEach { (title, route) ->
            TextButton(
                onClick = { navigator.goTo(route) },
                colors = ButtonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.Black
                )
            ) {
                Text(text = title)
            }
        }
    }
}

@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ComposePlaygroundTheme {
        HomeScreen(navigator = object : AppNavigator {
            override fun goTo(destination: NavKey) {}

            override fun goBack() {}
        })
    }
}