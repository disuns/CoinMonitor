package com.android.trade.domain.usecaseimpl

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.GetBinanceMarketUseCase
import com.android.trade.domain.usecase.GetBithumbMarketUseCase

class GetBinanceMarketUseCaseImpl(
    private val coinRepository: CoinRepository
) : GetBinanceMarketUseCase {
    override fun invoke()= coinRepository.fetchBinanceMarket()
}