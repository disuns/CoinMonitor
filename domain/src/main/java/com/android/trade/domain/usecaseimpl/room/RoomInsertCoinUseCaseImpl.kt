package com.android.trade.domain.usecaseimpl.room

import com.android.trade.domain.models.CoinInfo
import com.android.trade.domain.repositories.RoomRepository
import com.android.trade.domain.usecase.room.RoomInsertCoinUseCase

class RoomInsertCoinUseCaseImpl(private val roomRepository: RoomRepository
) : RoomInsertCoinUseCase {
    override suspend fun invoke(coinInfo: CoinInfo) = roomRepository.insertCoin(coinInfo)
}