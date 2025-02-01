package com.android.trade.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.android.trade.data.local.entity.CoinListEntity
import com.android.trade.data.local.entity.MarketEntity
import com.android.trade.data.local.entity.MarketWithCoins

@Dao
interface MarketDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarket(market: MarketEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCoins(coins: List<CoinListEntity>)

    @Transaction
    suspend fun insertMarketAndCoins(market: MarketEntity, coins: List<CoinListEntity>) {
        insertMarket(market)
        insertCoins(coins)
    }

    @Transaction
    @Query("SELECT * FROM market WHERE market = :marketName")
    suspend fun getMarketWithCoins(marketName: String): MarketWithCoins

    @Query("DELETE FROM market")
    suspend fun deleteAllMarkets()

    @Query("DELETE FROM coinList")
    suspend fun deleteAllCoins()
}