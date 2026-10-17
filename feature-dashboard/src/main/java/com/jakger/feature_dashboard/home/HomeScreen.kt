package com.jakger.feature_dashboard.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.jakger.composeplaygroudmobile.navigation.Buttons
import com.jakger.composeplaygroudmobile.navigation.Home
import com.jakger.composeplaygroudmobile.ui.theme.ComposePlaygroundTheme
import com.jakger.feature_googlemaps_api.GoogleMapsNavFlow
import kotlin.Pair
import kotlin.String

@Composable
fun HomeScreen(
    backStack: NavBackStack<NavKey>

) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val items = listOf<kotlin.Pair<kotlin.String, NavKey>>(
            stringResource(R.string.buttons_title) to Buttons,
            stringResource(R.string.googlemaps_title) to GoogleMapsNavFlow.GoogleMapsMainRoute
        )
        items.forEach { (title, route) ->
            TextButton(
                onClick = { backStack.add(route) },
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
        HomeScreen(backStack = rememberNavBackStack(Home))
    }
}