package com.android.trade.coinmonitor.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.android.trade.common.LoggerInitializer
import com.android.trade.common.utils.DARK_MODE_KEY
import com.android.trade.common.utils.dataStore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(){

    @Inject
    lateinit var loggerInitializer: LoggerInitializer

    override fun onCreate() {
        super.onCreate()

        val isDarkMode = runBlocking {
            dataStore.data.first()[DARK_MODE_KEY] ?: false
        }

        AppCompatDelegate.setDefaultNightMode(if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }
}