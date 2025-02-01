package com.android.trade.coinmonitor.di

import com.android.trade.data.implementations.CoinRepositoryImpl
import com.android.trade.data.implementations.RoomRepositoryImpl
import com.android.trade.data.manager.WebSocketManagerImpl
import com.android.trade.domain.WebSocketManager
import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.repositories.RoomRepository
import com.android.trade.presentation.viewmodels.RoomAndWebSocketViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCoinRepository(
        coinRepositoryImpl: CoinRepositoryImpl
    ): CoinRepository

    @Binds
    @Singleton
    abstract fun bindRoomRepository(
        roomRepositoryImpl: RoomRepositoryImpl
    ): RoomRepository
}