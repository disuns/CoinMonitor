package com.android.trade.domain.usecase.room

interface RoomDeleteCoinUseCase {
    suspend operator fun invoke(market: String, code: String)
}