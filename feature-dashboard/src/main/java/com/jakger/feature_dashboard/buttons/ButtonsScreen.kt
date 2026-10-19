package com.jakger.feature_dashboard.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ButtonsScreen() {

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Button(onClick = {}) {
                Text(text = "Sample Button")
            }
        }
    }
}

@Preview
@Composable
private fun ButtonsScreenPreview() {
    ButtonsScreen()
}