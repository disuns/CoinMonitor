package com.android.trade.coinmonitor.di

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.GetCoinUseCase
import com.android.trade.domain.usecaseimpl.GetCoinUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideGetCoinUseCase(
        coinRepository: CoinRepository
    ): GetCoinUseCase {
        return GetCoinUseCaseImpl(coinRepository)
    }
}