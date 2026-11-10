package com.jakger.ghcrange_mobile.ui.permissions

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jakger.feature_ghcrange_mobile.R

@Composable
fun PermissionsRationaleScreen(
    viewModel: PermissionsRationaleViewModel = hiltViewModel(),
    onFinish: (Boolean) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    BackHandler { viewModel.dispatchUiEvent(PermissionsRationaleUiEvent.Back) }

    LaunchedEffect(viewModel, onFinish) {
        viewModel.uiSideEffect.collect { effect ->
            if (effect is PermissionsRationaleUiSideEffect.Finish) {
                onFinish(effect.continueToPermissions)
            }
        }
    }

    PermissionsRationaleContent(state = state, onEvent = viewModel::dispatchUiEvent)
}

@Composable
internal fun PermissionsRationaleContent(
    state: PermissionsRationaleUiState,
    onEvent: (PermissionsRationaleUiEvent) -> Unit,
) {
    Scaffold { insets ->
        Column(
            modifier = Modifier.fillMaxSize().padding(insets),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f).widthIn(max = 600.dp).fillMaxWidth(),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                item {
                    Text(
                        text = stringResource(R.string.health_permissions_title),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
                item {
                    Text(
                        text = stringResource(R.string.health_permissions_message),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                item {
                    Text(
                        text = stringResource(R.string.health_permissions_data_usage),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                item {
                    Text(
                        text = stringResource(R.string.health_permissions_choice),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            Column(
                modifier = Modifier.widthIn(max = 600.dp).fillMaxWidth().padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (state.isPermissionRequest) {
                    Button(
                        onClick = { onEvent(PermissionsRationaleUiEvent.Continue) },
                        enabled = !state.isClosing,
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text(stringResource(R.string.health_permissions_continue)) }
                    TextButton(
                        onClick = { onEvent(PermissionsRationaleUiEvent.NotNow) },
                        enabled = !state.isClosing,
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text(stringResource(R.string.health_permissions_not_now)) }
                } else {
                    Button(
                        onClick = { onEvent(PermissionsRationaleUiEvent.Back) },
                        enabled = !state.isClosing,
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text(stringResource(R.string.health_permissions_back)) }
                }
            }
        }
    }
}
