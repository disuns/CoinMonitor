package com.android.trade.presentation.usecasegroup

import com.android.trade.domain.usecase.room.GetRoomAllCoinUseCase
import com.android.trade.domain.usecase.room.RoomDeleteCoinUseCase
import com.android.trade.domain.usecase.room.RoomInsertCoinUseCase
import javax.inject.Inject

class RoomCoinUseCaseGroup @Inject constructor(
    val insertCoin: RoomInsertCoinUseCase,
    val getAllCoin: GetRoomAllCoinUseCase,
    val deleteCoin: RoomDeleteCoinUseCase
)