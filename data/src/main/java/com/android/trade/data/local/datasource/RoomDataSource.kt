package com.android.trade.data.local.datasource

import com.android.trade.data.local.entity.CoinEntity
import com.android.trade.data.local.entity.CoinListEntity
import com.android.trade.domain.models.CoinInfo

interface RoomDataSource {
    suspend fun getCoin(market: String, code: String): CoinEntity?
    suspend fun insertCoin(coin: CoinEntity): String
    suspend fun getAllCoin(): MutableList<CoinEntity>
    suspend fun deleteCoin(market: String, code: String)

    suspend fun insertCoinList(coinInfoList: List<CoinInfo>)
    suspend fun getCoinList(market: String): List<CoinListEntity>
    suspend fun deleteCoinList()
}