package com.android.trade.domain.usecaseimpl

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.GetBithumbMarketUseCase

class GetBithumbMarketUseCaseImpl(
    private val coinRepository: CoinRepository
) : GetBithumbMarketUseCase {
    override fun invoke()= coinRepository.fetchBithumbMarket()
}