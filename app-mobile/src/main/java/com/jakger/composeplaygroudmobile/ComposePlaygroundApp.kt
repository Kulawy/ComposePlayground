package com.jakger.composeplaygroudmobile

import android.app.Application
import com.jakger.composeplaygroudmobile.logging.AndroidLogSink
import com.jakger.core_common.logging.AppLog
import com.jakger.core_common.logging.LogLevel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ComposePlaygroundApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            AppLog.configure(minimumLevel = LogLevel.DEBUG, sink = AndroidLogSink)
        } else {
            AppLog.disable()
        }
    }
}
