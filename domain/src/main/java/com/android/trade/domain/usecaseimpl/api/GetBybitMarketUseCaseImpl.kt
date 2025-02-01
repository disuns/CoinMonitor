package com.android.trade.domain.usecaseimpl.api

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.api.GetBybitMarketUseCase

class GetBybitMarketUseCaseImpl(
    private val coinRepository: CoinRepository
) : GetBybitMarketUseCase {
    override fun invoke()= coinRepository.fetchBybitMarket()
}