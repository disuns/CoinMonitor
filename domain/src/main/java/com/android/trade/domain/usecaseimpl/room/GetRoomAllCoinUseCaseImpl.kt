package com.android.trade.domain.usecaseimpl.room

import com.android.trade.domain.repositories.RoomRepository
import com.android.trade.domain.usecase.room.GetRoomAllCoinUseCase

class GetRoomAllCoinUseCaseImpl(private val roomRepository: RoomRepository
) : GetRoomAllCoinUseCase {
    override suspend fun invoke() = roomRepository.getAllCoin()
}