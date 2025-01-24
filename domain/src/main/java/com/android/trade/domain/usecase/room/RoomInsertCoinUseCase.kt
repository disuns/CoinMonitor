package com.android.trade.domain.usecase.room

import com.android.trade.domain.models.CoinInfo

interface RoomInsertCoinUseCase {
    suspend operator fun invoke(coinInfo: CoinInfo): String
}