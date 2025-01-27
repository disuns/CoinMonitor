package com.android.trade.data.local.datasource.impl

import com.android.trade.data.local.dao.CoinDao
import com.android.trade.data.local.datasource.RoomDataSource
import com.android.trade.data.local.entity.CoinEntity
import javax.inject.Inject

class RoomDataSourceImpl @Inject constructor(
    private val coinDao: CoinDao
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
}