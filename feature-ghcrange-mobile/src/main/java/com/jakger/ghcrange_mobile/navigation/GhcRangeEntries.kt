package com.jakger.ghcrange_mobile.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jakger.core_arch.navi.AppNavigator
import com.jakger.core_arch.navi.NavigationEntries
import com.jakger.feature_googlemaps_api.GhcRangeNavFlow
import com.jakger.ghcrange_mobile.ui.main.GhcRangeScreen
import com.jakger.ghcrange_mobile.ui.main.GhcRangeUiSideEffect
import com.jakger.ghcrange_mobile.ui.main.GhcRangeViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.collectLatest

class GhcRangeEntries @Inject constructor() : NavigationEntries {
    override fun install(
        scope: EntryProviderScope<NavKey>,
        navigator: AppNavigator
    ) = with(scope) {
        entry<GhcRangeNavFlow.Main> { key ->

            val viewModel = hiltViewModel<GhcRangeViewModel>()
            val lifecycleOwner = LocalLifecycleOwner.current

//            LaunchedEffect(viewModel, lifecycleOwner, navigator) {
//                lifecycleOwner.lifecycle.repeatOnLifecycle(
//                    Lifecycle.State.RESUMED
//                ) {
//                    viewModel.uiSideEffect.filterIsInstance(GhcRangeUiSideEffect.Navigation::class)
//                        .collectLatest { navigationEffect ->
//                            when (navigationEffect) {
//                                GhcRangeUiSideEffect.Navigation.Back ->
//                                    navigator.goBack()
//                            }
//                        }
//                }
//            }

            LaunchedEffect(viewModel.navigationEffects, lifecycleOwner, navigator) {
                lifecycleOwner.lifecycle.repeatOnLifecycle(
                    Lifecycle.State.RESUMED
                ) {
                    viewModel.navigationEffects.collectLatest { navigationEffect ->
                        when (navigationEffect) {
                            GhcRangeUiSideEffect.Navigation.Back ->
                                navigator.goBack()
                        }
                    }
                }
            }

            GhcRangeScreen(viewModel = viewModel)
        }
    }
}