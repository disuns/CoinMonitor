package com.coin.trade.coinmonitor.di

import com.android.trade.data.local.datasource.RoomDataSource
import com.android.trade.data.local.datasource.impl.RoomDataSourceImpl
import com.android.trade.data.remote.datasource.CoinDataSource
import com.android.trade.data.remote.datasource.impl.CoinDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindCoinDataSource(
        coinDataSourceImpl : CoinDataSourceImpl
    ): CoinDataSource

    @Binds
    @Singleton
    abstract fun bindRoomDataSource(
        roomDataSourceImpl: RoomDataSourceImpl
    ): RoomDataSource
}