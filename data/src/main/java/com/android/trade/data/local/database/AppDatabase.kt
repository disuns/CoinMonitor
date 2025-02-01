package com.android.trade.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.trade.data.local.dao.CoinDao
import com.android.trade.data.local.dao.MarketDao
import com.android.trade.data.local.entity.CoinEntity
import com.android.trade.data.local.entity.CoinListEntity
import com.android.trade.data.local.entity.MarketEntity

@Database(entities = [CoinEntity::class, CoinListEntity::class, MarketEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coinDao() : CoinDao
    abstract fun marketDao() : MarketDao
}