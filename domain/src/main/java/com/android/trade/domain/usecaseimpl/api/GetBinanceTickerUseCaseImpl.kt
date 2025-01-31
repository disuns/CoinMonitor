package com.android.trade.domain.usecaseimpl.api

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.api.GetBinanceTickerUseCase

class GetBinanceTickerUseCaseImpl(
    private val coinRepository: CoinRepository
) : GetBinanceTickerUseCase {
    override fun invoke(symbols : String) = coinRepository.fetchBinanceTicker(symbols)
}