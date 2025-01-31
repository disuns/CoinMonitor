package com.android.trade.domain.usecaseimpl.api

import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.api.GetBybitTickerUseCase

class GetBybitTickerUseCaseImpl(
    private val coinRepository: CoinRepository
) : GetBybitTickerUseCase {
    override fun invoke(symbol : String) = coinRepository.fetchBybitTicker(symbol)
}