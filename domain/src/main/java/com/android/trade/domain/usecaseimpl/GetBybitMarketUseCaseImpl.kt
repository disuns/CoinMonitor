package com.android.trade.domain.usecaseimpl

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.GetBithumbMarketUseCase
import com.android.trade.domain.usecase.GetBybitMarketUseCase

class GetBybitMarketUseCaseImpl(
    private val coinRepository: CoinRepository
) : GetBybitMarketUseCase {
    override fun invoke()= coinRepository.fetchBybitMarket()
}