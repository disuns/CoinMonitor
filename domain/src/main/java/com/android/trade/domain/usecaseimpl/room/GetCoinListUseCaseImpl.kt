package com.android.trade.domain.usecaseimpl.room

import com.android.trade.domain.repositories.RoomRepository
import com.android.trade.domain.usecase.room.GetCoinListUseCase
import com.android.trade.domain.usecase.room.GetRoomAllCoinUseCase

class GetCoinListUseCaseImpl(private val roomRepository: RoomRepository
) : GetCoinListUseCase {
    override suspend fun invoke(market : String) = roomRepository.getCoinList(market)
}