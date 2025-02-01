package com.android.trade.domain.usecaseimpl.api

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.api.GetUpbitMarketUseCase

class GetUpbitMarketUseCaseImpl(
    private val coinRepository: CoinRepository
) : GetUpbitMarketUseCase {
    override fun invoke() = coinRepository.fetchUpbitMarket()
}