package com.jakger.feature_dashboard.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jakger.feature_dashboard.home.HomeViewModel

@Composable
fun ButtonsScreen(
    val viewModel: HomeViewModel = hiltViewModel()
) {

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Button(onClick = {}) {
                Text(text = "dawd")
            }
        }
    }
}

@Preview
@Composable
private fun ButtonsScreenPreview() {
    ButtonsScreen()
}