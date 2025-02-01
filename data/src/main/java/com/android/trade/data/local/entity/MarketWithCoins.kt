package com.android.trade.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class MarketWithCoins(
    @Embedded val market: MarketEntity,

    @Relation(
        parentColumn = "market",
        entityColumn = "market"
    )
    val coins: List<CoinListEntity>
)