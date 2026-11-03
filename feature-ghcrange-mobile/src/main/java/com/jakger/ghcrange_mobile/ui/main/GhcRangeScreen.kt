package com.jakger.ghcrange_mobile.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jakger.core_designsystem.ui.components.InfinityTrail

/***
 * Google Health Connect Range Start Screen
 */
@Composable
fun GhcRangeScreen(
    modifier: Modifier = Modifier,
    viewModel: GhcRangeViewModel = hiltViewModel()
) {
    val themeGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.secondary
        )
    )
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .background(brush = themeGradient)
            .fillMaxSize()
    ) {
        when (val screenState = state.value) {
            is GhcRangeUiState.Initial, GhcRangeUiState.Loading -> InfinityTrail(
                modifier = modifier.align(Alignment.Center)
            )

            is GhcRangeUiState.View -> DisplayForm(
                state = screenState,
                onEvent = { event -> viewModel.dispatchUiEvent(event) }
            )

            is GhcRangeUiState.Error -> {

            }

            else -> {}
        }
    }
}

@Composable
private fun DisplayForm(
    modifier: Modifier = Modifier,
    state: GhcRangeUiState.View,
    onEvent: (GhcRangeUiEvent) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxSize(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        val records = state.activityRecords
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Activity Data",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            items(records) { record ->
                OutlinedTextField(
                    value = record.value,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text(record.name) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )
            }

//            OutlinedTextField(
//                value = "email",
//                onValueChange = { },
//                readOnly = true,
//                label = { Text("Adres e-mail") },
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true,
//                shape = MaterialTheme.shapes.medium
//            )

            // Pole: Hasło
//                OutlinedTextField(
//                    value = "password",
//                    onValueChange = {  },
//                    label = { "Hasło" },
//                    leadingIcon = {
//                        Icon(Icons.Default.Lock, contentDescription = "Hasło")
//                    },
//                    trailingIcon = {
//                        val image = if (passwordVisible)
//                            Icons.Filled.Visibility
//                        else
//                            Icons.Filled.VisibilityOff
//                    },
//                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                    modifier = Modifier.fillMaxWidth(),
//                    singleLine = true,
//                    shape = MaterialTheme.shapes.medium
//                ){
//                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                        Icon(imageVector = image, contentDescription = "Pokaż/ukryj hasło")
//                    }
//                }
        }
    }
}