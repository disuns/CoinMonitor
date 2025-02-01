package com.android.trade.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "market")
data class MarketEntity(
    @PrimaryKey val market: String
)