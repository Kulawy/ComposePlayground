package com.jakger.ghcrange_mobile.ui.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jakger.core_common.logging.ComposeLog
import com.jakger.core_designsystem.ui.components.InfinityTrail
import com.jakger.feature_ghcrange_mobile.R
import com.jakger.core_designsystem.R.dimen as dsDim

/***
 * Google Health Connect Range Start Screen
 */

val healthPermissions = setOf(
    HealthPermission.getReadPermission(StepsRecord::class),
    HealthPermission.getReadPermission(HeartRateRecord::class),
    HealthPermission.getReadPermission(BloodPressureRecord::class),
    HealthPermission.getReadPermission(DistanceRecord::class),
    HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class)
)

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

    val requestPermissionContract = PermissionController.createRequestPermissionResultContract()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = requestPermissionContract
    ) { grantedPermissions ->
        ComposeLog.d { "Health Connect permission result received" }
//        onPermissionsResult(grantedPermissions)
        if (grantedPermissions.containsAll(healthPermissions)) {
            viewModel.dispatchUiIntent(GhcRangeUiEvent.HealthPermissionsGranted)
        } else {
            viewModel.dispatchUiIntent(GhcRangeUiEvent.HealthPermissionsDenied)
        }
    }

//    display permissions dialog
//    permissionLauncher.launch(healthPermissions)

    Box(
        modifier = Modifier
            .statusBarsPadding()
            .background(brush = themeGradient)
            .fillMaxSize(),
    ) {
        when (val screenState = state.value) {
            is GhcRangeUiState.Initial, GhcRangeUiState.Loading -> InfinityTrail(
                modifier = modifier.align(Alignment.Center),
            )

            is GhcRangeUiState.Display -> DisplayForm(
                state = screenState,
                onEvent = { event -> viewModel.dispatchUiIntent(event) }
            )

            is GhcRangeUiState.Error -> {
                DisplayErrorCard(
                    modifier = modifier
                        .align(Alignment.TopCenter),
                    state = screenState,
                    onEvent = { event -> viewModel.dispatchUiIntent(event) }
                )
            }
        }
    }
}

@Composable
private fun DisplayForm(
    modifier: Modifier = Modifier,
    state: GhcRangeUiState.Display,
    onEvent: (GhcRangeUiEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
//        val records = state.activityRecords
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxSize(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                Text(
                    text = state.aggregatedHeartRate.avgBpm.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = state.aggregatedHeartRate.maxBpm.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = state.aggregatedHeartRate.minBpm.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = state.aggregatedHeartRate.measurementsCount.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
//            items(records) { record ->
//                OutlinedTextField(
//                    value = record.value,
//                    onValueChange = { },
//                    readOnly = true,
//                    label = { Text(record.name) },
//                    modifier = Modifier.fillMaxWidth(),
//                    singleLine = true,
//                    shape = MaterialTheme.shapes.medium
//                )
//            }

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

@Composable
private fun DisplayErrorCard(
    modifier: Modifier = Modifier,
    state: GhcRangeUiState.Error,
    onEvent: (GhcRangeUiEvent) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(fraction = 0.9f),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp)
    ) {
        val text = when (state) {
            is GhcRangeUiState.Error.PermissionsNotGranted -> stringResource(R.string.ghc_range_error_permissions_not_granted)
            is GhcRangeUiState.Error.HealthDataNotAvailable -> stringResource(R.string.ghc_range_error_service_unavailable)
        }
        Text(
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(dsDim.margin_16)),
            text = stringResource(R.string.error_card_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
        Text(
            modifier = modifier.padding(dimensionResource(dsDim.margin_16)),
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}
