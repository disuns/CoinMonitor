package com.android.trade.common

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoggerInitializer @Inject constructor() {
    init{
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(5)
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }
}