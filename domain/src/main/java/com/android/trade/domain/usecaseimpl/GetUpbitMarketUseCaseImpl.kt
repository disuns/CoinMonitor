package com.android.trade.domain.usecaseimpl

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.GetUpbitMarketUseCase

class GetUpbitMarketUseCaseImpl(
    private val coinRepository: CoinRepository
) : GetUpbitMarketUseCase {
    override fun invoke() = coinRepository.fetchUpbitMarket()
}