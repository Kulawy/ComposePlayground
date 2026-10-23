package com.jakger.feature_dashboard.home

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation3.runtime.NavKey
import com.jakger.core_arch.navi.AppNavigator
import com.jakger.core_designsystem.ui.theme.ComposePlaygroundTheme
import com.jakger.feature_dashboard.R
import com.jakger.feature_dashboard.utils.checkLocationPermissions
import com.jakger.feature_dashboard_api.DashboardNavigationFlow
import com.jakger.feature_googlemaps_api.GhcRangeNavFlow
import com.jakger.feature_maplibremaps_api.MapLibreMapsNavigationFlow

@Composable
fun HomeScreen(
    navigator: AppNavigator
) {
    val context = LocalContext.current
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var hasLocationPermission by remember {
        mutableStateOf(context.checkLocationPermissions(locationPermissions))
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineGranted) {
            hasLocationPermission = true
            Toast.makeText(context, "Przyznano dokładną lokalizację", Toast.LENGTH_SHORT).show()
        } else if (coarseGranted) {
            hasLocationPermission = true
            Toast.makeText(context, "Przyznano tylko przybliżoną lokalizację", Toast.LENGTH_SHORT).show()
        } else {
            hasLocationPermission = false
            Toast.makeText(context, "Odrzucono dostęp do lokalizacji", Toast.LENGTH_SHORT).show()
        }
    }

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
            stringResource(R.string.google_health_connect_title) to GhcRangeNavFlow.Main,
            stringResource(R.string.openstreetmaps_title) to MapLibreMapsNavigationFlow.Main
        )
        Text(
            text = stringResource(R.string.dashboard_title),
            style = MaterialTheme.typography.titleLarge
        )
        items.forEach { (title, route) ->
            TextButton(
                onClick = {
                    if (route is MapLibreMapsNavigationFlow){
                        if (hasLocationPermission){
                            navigator.goTo(route)
                        } else {
                            locationPermissionLauncher.launch(locationPermissions)
                        }
                    } else {
                        navigator.goTo(route)
                    }
                },
                colors = ButtonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.Black
                )
            ) {
                Text(text = title)
            }
            if (route is MapLibreMapsNavigationFlow && !hasLocationPermission){
                Text(
                    text = stringResource(R.string.localization_permissions_required),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Red
                )
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