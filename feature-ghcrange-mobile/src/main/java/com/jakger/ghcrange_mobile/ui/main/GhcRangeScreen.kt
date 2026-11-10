package com.jakger.ghcrange_mobile.ui.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.capitalize
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.jakger.core_common.logging.ComposeLog
import com.jakger.core_designsystem.ui.components.InfinityTrail
import com.jakger.feature_ghcrange_mobile.R
import com.jakger.ghcrange_mobile.ui.permissions.PermissionsRationaleContract
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
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.background
        )
    )

    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    BackHandler { viewModel.dispatchUiEvent(GhcRangeUiEvent.OnBackPressed) }

    val rationaleLauncher =
        rememberLauncherForActivityResult(PermissionsRationaleContract()) { accepted ->
            viewModel.dispatchUiEvent(
                if (accepted) GhcRangeUiEvent.RequestPermission
                else GhcRangeUiEvent.HealthPermissionsDenied
            )
        }
    val settingsLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.dispatchUiEvent(GhcRangeUiEvent.PermissionSettingsClosed)
        }

    val requestPermissionContract = PermissionController.createRequestPermissionResultContract()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = requestPermissionContract
    ) { grantedPermissions ->
        ComposeLog.d { "Health Connect permission Launcher result received" }
        if (grantedPermissions.containsAll(healthPermissions)) {
            ComposeLog.d { "Health Connect permission Launcher GRANTED" }
            viewModel.dispatchUiEvent(GhcRangeUiEvent.HealthPermissionsGranted)
        } else {
            ComposeLog.d { "Health Connect permission Launcher DENIED" }
            viewModel.dispatchUiEvent(GhcRangeUiEvent.HealthPermissionsDenied)
        }
    }

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.dispatchUiEvent(GhcRangeUiEvent.ScreenResumed)
            viewModel.uiEffects.collect { effect ->
                if (GhcRangePermissionStage.from(viewModel.uiState.value)?.pendingEffect != effect) {
                    return@collect
                }
                viewModel.dispatchUiEvent(GhcRangeUiEvent.PermissionUiEffectLaunched(effect))
                ComposeLog.d { "Health Connect Effect received: $effect" }

                try {
                    when (effect) {
                        GhcRangeUiSideEffect.UiEffect.ExplainPermissions -> rationaleLauncher.launch(
                            Unit
                        )

                        GhcRangeUiSideEffect.UiEffect.RequestPermissions -> permissionLauncher.launch(
                            healthPermissions
                        )

                        GhcRangeUiSideEffect.UiEffect.OpenPermissionSettings -> {
                            try {
                                settingsLauncher.launch(Intent(HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS))
                            } catch (_: ActivityNotFoundException) {
                                settingsLauncher.launch(
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        .setData(Uri.parse("package:${context.packageName}"))
                                )
                            }
                        }
                    }
                } catch (exception: ActivityNotFoundException) {
                    ComposeLog.e(throwable = exception) { "Health Connect permission activity is unavailable" }
                    viewModel.dispatchUiEvent(GhcRangeUiEvent.PermissionUiLaunchFailed)
                } catch (exception: SecurityException) {
                    ComposeLog.e(throwable = exception) { "Health Connect permission activity could not be opened" }
                    viewModel.dispatchUiEvent(GhcRangeUiEvent.PermissionUiLaunchFailed)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .background(brush = themeGradient)
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        when (val screenState = state.value) {
            is GhcRangeUiState.Initial -> {
                InfinityTrail(
                    modifier = modifier.align(Alignment.Center),
                )
            }

            is GhcRangeUiState.AwaitingPermissionExplanation,
            is GhcRangeUiState.AwaitingPermissionResult,
            is GhcRangeUiState.AwaitingPermissionSettings,
            is GhcRangeUiState.Loading -> InfinityTrail(
                modifier = modifier.align(Alignment.Center),
            )

            is GhcRangeUiState.Display -> DisplayForm(
                state = screenState,
                onEvent = { event -> viewModel.dispatchUiEvent(event) }
            )

            is GhcRangeUiState.Error -> {
                DisplayErrorCard(
                    modifier = modifier
                        .align(Alignment.TopCenter),
                    state = screenState,
                    onEvent = { event -> viewModel.dispatchUiEvent(event) }
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
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(dsDim.margin_24)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(dsDim.margin_8))
    ) {
        item {
            ElevatedCard(
                modifier = Modifier
                    .padding(top = dimensionResource(dsDim.margin_16))
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.extraLarge,
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 64.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme
                        .surfaceContainerHigh.copy(alpha = 0.8f)
                ),
            ) {
                val listItemRowModifier = Modifier
                    .padding(
                        horizontal = dimensionResource(dsDim.margin_16)
                    )
                    .fillMaxWidth()
                Row(
                    modifier = listItemRowModifier
                        .padding(top = dimensionResource(dsDim.margin_16)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(R.string.record_heart_rate_section_description).uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier,
                        text = stringResource(R.string.period_description_yesterday).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    AnimatedHeart()
                }
                RecordCardItemDivider()
                RecordCardItem(
                    modifier = listItemRowModifier,
                    recordTitle = stringResource(R.string.record_description_avg_heart_rate),
                    recordValue = state.aggregatedHeartRate.avgBpm,
                )
                RecordCardItemDivider()
                RecordCardItem(
                    modifier = listItemRowModifier,
                    recordTitle = stringResource(R.string.record_description_max_heart_rate),
                    recordValue = state.aggregatedHeartRate.maxBpm,
                )
                RecordCardItemDivider()
                RecordCardItem(
                    modifier = listItemRowModifier,
                    recordTitle = stringResource(R.string.record_description_min_heart_rate),
                    recordValue = state.aggregatedHeartRate.minBpm,
                )
                RecordCardItemDivider()
                RecordCardItem(
                    modifier = listItemRowModifier
                        .padding(bottom = dimensionResource(dsDim.margin_16)),
                    recordTitle = stringResource(R.string.record_description_measurements_count),
                    recordValue = state.aggregatedHeartRate.measurementsCount
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
        if (state == GhcRangeUiState.Error.PermissionsNotGranted) {
            Button(
                onClick = { onEvent(GhcRangeUiEvent.CheckHealthDataAvailability) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) { Text(stringResource(R.string.health_permissions_retry)) }
            TextButton(
                onClick = { onEvent(GhcRangeUiEvent.ManagePermissions) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) { Text(stringResource(R.string.health_permissions_settings)) }
        }
        TextButton(
            onClick = { onEvent(GhcRangeUiEvent.OnBackPressed) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) { Text(stringResource(R.string.health_permissions_leave)) }
    }
}

@Composable
fun RecordCardItem(
    modifier: Modifier = Modifier,
    recordTitle: String,
    recordValue: String,
) {
    Row(
        modifier = modifier.padding(vertical = dimensionResource(dsDim.margin_4)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = recordTitle,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = recordValue,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ColumnScope.RecordCardItemDivider(modifier: Modifier = Modifier) = HorizontalDivider(
    modifier = modifier
        .fillMaxWidth(0.9f)
        .align(Alignment.CenterHorizontally),
    thickness = 1.dp,
    color = Color.DarkGray
)

@Composable
fun AnimatedHeart(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "HeartbeatTransition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1200
                1.0f at 0
                1.333f at 140 using FastOutSlowInEasing
                1.12f at 260 using FastOutLinearInEasing
                1.23f at 400 using FastOutSlowInEasing
                1.0f at 600 using LinearOutSlowInEasing
                1.0f at 1200
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "HeartbeatScale"
    )

    Icon(
        painter = painterResource(R.drawable.ic_ecg_heart_48),
        contentDescription = "Animated Heart",
        tint = Color.White,
        modifier = modifier
            .size(48.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin.Center
            }
    )
}