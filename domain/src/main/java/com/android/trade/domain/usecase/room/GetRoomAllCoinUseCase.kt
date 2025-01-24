package com.android.trade.domain.usecase.room

import com.android.trade.domain.models.CoinInfo

interface GetRoomAllCoinUseCase {
    suspend operator fun invoke(): MutableList<CoinInfo>
}