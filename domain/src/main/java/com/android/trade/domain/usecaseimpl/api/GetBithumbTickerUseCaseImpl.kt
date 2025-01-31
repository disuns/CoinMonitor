package com.android.trade.domain.usecaseimpl.api

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.api.GetBithumbTickerUseCase

class GetBithumbTickerUseCaseImpl(
    private val coinRepository: CoinRepository
) : GetBithumbTickerUseCase {
    override fun invoke(markets : String) = coinRepository.fetchBithumbTicker(markets)
}