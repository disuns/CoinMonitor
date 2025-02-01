package com.android.trade.domain.usecase.room

import com.android.trade.domain.models.CoinInfo

interface RoomInsertCoinListUseCase {
    suspend operator fun invoke(coinInfoList: List<CoinInfo>)
}