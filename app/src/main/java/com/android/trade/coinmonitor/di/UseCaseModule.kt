package com.android.trade.coinmonitor.di

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.GetUpbitMarketUseCase
import com.android.trade.domain.usecaseimpl.GetUpbitMarketUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideGetUpbitMarketUseCase(
        coinRepository: CoinRepository
    ): GetUpbitMarketUseCase {
        return GetUpbitMarketUseCaseImpl(coinRepository)
    }
}