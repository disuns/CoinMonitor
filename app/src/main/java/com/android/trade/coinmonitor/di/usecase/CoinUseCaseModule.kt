package com.android.trade.coinmonitor.di.usecase

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.api.GetBinanceMarketUseCase
import com.android.trade.domain.usecase.api.GetBinanceTickerUseCase
import com.android.trade.domain.usecase.api.GetBithumbMarketUseCase
import com.android.trade.domain.usecase.api.GetBithumbTickerUseCase
import com.android.trade.domain.usecase.api.GetBybitMarketUseCase
import com.android.trade.domain.usecase.api.GetBybitTickerUseCase
import com.android.trade.domain.usecase.api.GetUpbitMarketUseCase
import com.android.trade.domain.usecase.api.GetUpbitTickerUseCase
import com.android.trade.domain.usecase.room.GetCoinListUseCase
import com.android.trade.domain.usecase.room.GetRoomAllCoinUseCase
import com.android.trade.domain.usecase.room.RoomDeleteCoinListUseCase
import com.android.trade.domain.usecase.room.RoomDeleteCoinUseCase
import com.android.trade.domain.usecase.room.RoomInsertCoinListUseCase
import com.android.trade.domain.usecase.room.RoomInsertCoinUseCase
import com.android.trade.domain.usecaseimpl.api.GetBinanceMarketUseCaseImpl
import com.android.trade.domain.usecaseimpl.api.GetBinanceTickerUseCaseImpl
import com.android.trade.domain.usecaseimpl.api.GetBithumbMarketUseCaseImpl
import com.android.trade.domain.usecaseimpl.api.GetBithumbTickerUseCaseImpl
import com.android.trade.domain.usecaseimpl.api.GetBybitMarketUseCaseImpl
import com.android.trade.domain.usecaseimpl.api.GetBybitTickerUseCaseImpl
import com.android.trade.domain.usecaseimpl.api.GetUpbitMarketUseCaseImpl
import com.android.trade.domain.usecaseimpl.api.GetUpbitTickerUseCaseImpl
import com.android.trade.presentation.usecasegroup.CoinMarketUseCaseGroup
import com.android.trade.presentation.usecasegroup.CoinTickerUseCaseGroup
import com.android.trade.presentation.usecasegroup.RoomCoinListUseCaseGroup
import com.android.trade.presentation.usecasegroup.RoomCoinUseCaseGroup
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object CoinUseCaseModule {

    @Provides
    fun provideCoinMarketUseCaseGroup(
        getUpbitMarket: GetUpbitMarketUseCase,
        getBithumbMarket: GetBithumbMarketUseCase,
        getBinanceMarket: GetBinanceMarketUseCase,
        getBybitMarket: GetBybitMarketUseCase
    ): CoinMarketUseCaseGroup {
        return CoinMarketUseCaseGroup(getUpbitMarket, getBithumbMarket, getBinanceMarket, getBybitMarket)
    }

    @Provides
    fun provideCoinTickerUseCaseGroup(
        getUpbitTicker: GetUpbitTickerUseCase,
        getBithumbTicker: GetBithumbTickerUseCase,
        getBinanceTicker: GetBinanceTickerUseCase,
        getBybitTicker: GetBybitTickerUseCase
    ): CoinTickerUseCaseGroup {
        return CoinTickerUseCaseGroup(getUpbitTicker, getBithumbTicker, getBinanceTicker, getBybitTicker)
    }

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

    @Provides
    @ViewModelScoped
    fun provideGetUpbitTickerUseCase(
        coinRepository: CoinRepository
    ): GetUpbitTickerUseCase {
        return GetUpbitTickerUseCaseImpl(coinRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetBithumbTickerUseCase(
        coinRepository: CoinRepository
    ): GetBithumbTickerUseCase {
        return GetBithumbTickerUseCaseImpl(coinRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetBinanceTickerUseCase(
        coinRepository: CoinRepository
    ): GetBinanceTickerUseCase {
        return GetBinanceTickerUseCaseImpl(coinRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetGetBybitTickerUseCase(
        coinRepository: CoinRepository
    ): GetBybitTickerUseCase {
        return GetBybitTickerUseCaseImpl(coinRepository)
    }
}