package com.android.trade.coinmonitor.di.usecase

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.repositories.RoomRepository
import com.android.trade.domain.usecase.GetBinanceMarketUseCase
import com.android.trade.domain.usecase.GetBithumbMarketUseCase
import com.android.trade.domain.usecase.GetBybitMarketUseCase
import com.android.trade.domain.usecase.GetUpbitMarketUseCase
import com.android.trade.domain.usecase.room.GetRoomAllCoinUseCase
import com.android.trade.domain.usecase.room.RoomDeleteCoinUseCase
import com.android.trade.domain.usecase.room.RoomInsertCoinUseCase
import com.android.trade.domain.usecaseimpl.GetBinanceMarketUseCaseImpl
import com.android.trade.domain.usecaseimpl.GetBithumbMarketUseCaseImpl
import com.android.trade.domain.usecaseimpl.GetBybitMarketUseCaseImpl
import com.android.trade.domain.usecaseimpl.GetUpbitMarketUseCaseImpl
import com.android.trade.domain.usecaseimpl.room.GetRoomAllCoinUseCaseImpl
import com.android.trade.domain.usecaseimpl.room.RoomDeleteCoinUseCaseImpl
import com.android.trade.domain.usecaseimpl.room.RoomInsertCoinUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object CoinUseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideGetUpbitMarketUseCase(
        coinRepository: CoinRepository
    ): GetUpbitMarketUseCase {
        return GetUpbitMarketUseCaseImpl(coinRepository)
    }
    @Provides
    @ViewModelScoped
    fun provideGetBithumbMarketUseCase(
        coinRepository: CoinRepository
    ): GetBithumbMarketUseCase {
        return GetBithumbMarketUseCaseImpl(coinRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetBinanceMarketUseCase(
        coinRepository: CoinRepository
    ): GetBinanceMarketUseCase {
        return GetBinanceMarketUseCaseImpl(coinRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetBybitMarketUseCase(
        coinRepository: CoinRepository
    ): GetBybitMarketUseCase {
        return GetBybitMarketUseCaseImpl(coinRepository)
    }
}