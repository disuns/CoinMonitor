package com.android.trade.domain.usecaseimpl.room

import com.android.trade.domain.repositories.RoomRepository
import com.android.trade.domain.usecase.room.RoomDeleteCoinUseCase

class RoomDeleteCoinUseCaseImpl(private val roomRepository: RoomRepository
): RoomDeleteCoinUseCase {
    override suspend fun invoke(market: String, code: String) = roomRepository.deleteCoin(market, code)
}