package com.android.trade.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.trade.data.local.entity.CoinEntity

@Dao
interface CoinDao {
    @Query("SELECT * FROM coin WHERE market = :market AND code = :code LIMIT 1")
    suspend fun getCoinByMarketAndCode(market: String, code: String): CoinEntity?

    @Query("SELECT * FROM coin")
    suspend fun getAllCoin(): MutableList<CoinEntity>

    @Query("SELECT COUNT(*) FROM coin")
    suspend fun getCoinCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCoin(coin: CoinEntity)

    @Query("DELETE FROM coin WHERE market = :market AND code = :code")
    suspend fun deleteCoinByMarketAndCode(market: String, code: String)
}