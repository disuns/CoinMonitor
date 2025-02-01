package com.android.trade.domain.repositories

import com.android.trade.domain.models.CoinInfo

interface RoomRepository {
    suspend fun getCoin(market: String, code: String): CoinInfo?
    suspend fun insertCoin(coinInfo: CoinInfo): String
    suspend fun getAllCoin(): MutableList<CoinInfo>
    suspend fun deleteCoin(market: String, code: String)
    suspend fun insertCoinList(coinInfoList: List<CoinInfo>)
    suspend fun getCoinList(market: String): List<CoinInfo?>
    suspend fun deleteCoinList()
}