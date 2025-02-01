package com.android.trade.common.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.orhanobut.logger.Logger

fun Any.logMessage(message: Any?, tag: String = "CoinMonitor") {
    Logger.t(tag).e(message.toString())
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")