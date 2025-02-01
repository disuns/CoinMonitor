package com.android.trade.presentation.usecasegroup

import com.android.trade.domain.usecase.room.GetCoinListUseCase
import com.android.trade.domain.usecase.room.RoomDeleteCoinListUseCase
import com.android.trade.domain.usecase.room.RoomInsertCoinListUseCase
import javax.inject.Inject

class RoomCoinListUseCaseGroup @Inject constructor(
    val insertCoinList: RoomInsertCoinListUseCase,
    val getCoinList : GetCoinListUseCase,
    val deleteCoinList: RoomDeleteCoinListUseCase
)