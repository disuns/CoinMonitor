package com.coin.trade.coinmonitor.di

import com.android.trade.data.manager.WebSocketManagerImpl
import com.android.trade.domain.WebSocketManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    @Singleton
    abstract fun bindWebSocketManager(
        manager: WebSocketManagerImpl
    ): WebSocketManager
}