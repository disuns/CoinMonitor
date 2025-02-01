package com.android.trade.data.local.datasource.impl

import com.android.trade.common.utils.logMessage
import com.android.trade.data.local.dao.CoinDao
import com.android.trade.data.local.dao.MarketDao
import com.android.trade.data.local.datasource.RoomDataSource
import com.android.trade.data.local.entity.CoinEntity
import com.android.trade.data.local.entity.CoinListEntity
import com.android.trade.data.local.entity.MarketEntity
import com.android.trade.domain.models.CoinInfo
import javax.inject.Inject

class RoomDataSourceImpl @Inject constructor(
    private val coinDao: CoinDao,
    private val marketDao: MarketDao
) : RoomDataSource {
    override suspend fun getAllCoin() = coinDao.getAllCoin()

    override suspend fun getCoin(market : String, code : String) = coinDao.getCoinByMarketAndCode(market, code)

    override suspend fun deleteCoin(market : String, code : String) = coinDao.deleteCoinByMarketAndCode(market, code)

    override suspend fun insertCoin(coin : CoinEntity): String {
        return if(coinDao.getCoinCount() >= 3){
            "갯수 상한선"
        }else if(coinDao.getCoinByMarketAndCode(coin.market, coin.code) != null){
            "데이터가 이미 존재합니다."
        }else{
            coinDao.insertCoin(coin)
            ""
        }
    }

    override suspend fun insertCoinList(coinInfoList: List<CoinInfo>) {
        val markets = coinInfoList.map { it.market }.distinct().map { MarketEntity(it) }
        val coins = coinInfoList.map { CoinListEntity(code = it.code, market = it.market, coinName = it.coinName) }

        markets.forEach { market ->
            val marketCoins = coins.filter { it.market == market.market }
            marketDao.insertMarketAndCoins(market, marketCoins)
        }
    }

    override suspend fun getCoinList(market: String) = marketDao.getMarketWithCoins(market).coins


    override suspend fun deleteCoinList() {
        marketDao.deleteAllCoins()
        marketDao.deleteAllMarkets()
    }
}