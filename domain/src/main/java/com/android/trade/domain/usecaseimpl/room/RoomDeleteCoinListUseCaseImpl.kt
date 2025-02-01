package com.android.trade.domain.usecaseimpl.room

import com.android.trade.domain.repositories.RoomRepository
import com.android.trade.domain.usecase.room.GetCoinListUseCase
import com.android.trade.domain.usecase.room.GetRoomAllCoinUseCase
import com.android.trade.domain.usecase.room.RoomDeleteCoinListUseCase

class RoomDeleteCoinListUseCaseImpl(private val roomRepository: RoomRepository
) : RoomDeleteCoinListUseCase {
    override suspend fun invoke() = roomRepository.deleteCoinList()
}