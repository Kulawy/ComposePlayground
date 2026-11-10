package com.jakger.ghcrange_mobile.ui.permissions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jakger.core_designsystem.ui.theme.ComposePlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionsRationaleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlaygroundTheme(dynamicColor = false) {
                PermissionsRationaleScreen { continueToPermissions ->
                    setResult(if (continueToPermissions) RESULT_OK else RESULT_CANCELED)
                    finish()
                }
            }
        }
    }
}
