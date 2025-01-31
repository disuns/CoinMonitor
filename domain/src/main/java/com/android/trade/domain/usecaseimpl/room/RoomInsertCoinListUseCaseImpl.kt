package com.android.trade.domain.usecaseimpl.room

import com.android.trade.domain.models.CoinInfo
import com.android.trade.domain.repositories.RoomRepository
import com.android.trade.domain.usecase.room.GetCoinListUseCase
import com.android.trade.domain.usecase.room.GetRoomAllCoinUseCase
import com.android.trade.domain.usecase.room.RoomInsertCoinListUseCase

class RoomInsertCoinListUseCaseImpl(private val roomRepository: RoomRepository
) : RoomInsertCoinListUseCase {
    override suspend fun invoke(coinInfoList : List<CoinInfo>) = roomRepository.insertCoinList(coinInfoList)
}