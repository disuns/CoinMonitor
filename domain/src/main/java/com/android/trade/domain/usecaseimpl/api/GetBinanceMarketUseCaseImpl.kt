package com.android.trade.domain.usecaseimpl.api

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.api.GetBinanceMarketUseCase

class GetBinanceMarketUseCaseImpl(
    private val coinRepository: CoinRepository
) : GetBinanceMarketUseCase {
    override fun invoke()= coinRepository.fetchBinanceMarket()
}