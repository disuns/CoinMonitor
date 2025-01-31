package com.android.trade.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "coinList",
    foreignKeys = [ForeignKey(
        entity = MarketEntity::class,
        parentColumns = ["market"],
        childColumns = ["market"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["market"])]
)
data class CoinListEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val code: String,
    val market: String,
    val coinName: String
)