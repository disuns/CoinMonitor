package com.android.trade.domain.usecase.room

import com.android.trade.domain.models.CoinInfo

interface GetCoinListUseCase {
    suspend operator fun invoke(market: String): List<CoinInfo?>
}