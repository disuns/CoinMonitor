package com.android.trade.common.utils

import com.orhanobut.logger.Logger

fun Any.logMessage(message: Any?, tag: String = "CoinMonitor") {
    Logger.t(tag).e(message.toString())
}