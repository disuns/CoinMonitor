package com.android.trade.common.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.orhanobut.logger.BuildConfig
import com.orhanobut.logger.Logger

fun Any.logMessage(message: Any?, tag: String = "CoinMonitor") {
//    if(BuildConfig.DEBUG)
        Logger.t(tag).e(message.toString())
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")