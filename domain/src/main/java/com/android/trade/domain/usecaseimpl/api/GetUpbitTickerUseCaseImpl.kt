package com.android.trade.domain.usecaseimpl.api

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.api.GetUpbitTickerUseCase

class GetUpbitTickerUseCaseImpl(
    private val coinRepository: CoinRepository
) : GetUpbitTickerUseCase {
    override fun invoke(markets : String) = coinRepository.fetchUpbitTicker(markets)
}