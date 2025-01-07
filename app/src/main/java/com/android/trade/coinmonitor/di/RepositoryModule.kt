package com.android.trade.coinmonitor.di

import com.android.trade.data.implementations.CoinRepositoryImpl
import com.android.trade.data.local.datasource.LocalDataSource
import com.android.trade.data.mapper.CoinDataMapper
import com.android.trade.data.remote.datasource.CoinDataSource
import com.android.trade.domain.repositories.CoinRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideCoinRepository(
        localDataSource: LocalDataSource,
        coinDataSource: CoinDataSource,
        mapper: CoinDataMapper
    ): CoinRepository {
        return CoinRepositoryImpl(
            localDataSource = localDataSource,
            coinDataSource = coinDataSource,
            mapper = mapper
        )
    }
}