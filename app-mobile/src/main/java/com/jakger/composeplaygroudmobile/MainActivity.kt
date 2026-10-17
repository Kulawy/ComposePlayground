package com.jakger.composeplaygroudmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jakger.composeplaygroudmobile.navigation.NavigationRoot
import com.jakger.core_arch.navi.NavigationEntries
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var entries: Set<@JvmSuppressWildcards NavigationEntries>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationRoot(entries)
        }
    }
}