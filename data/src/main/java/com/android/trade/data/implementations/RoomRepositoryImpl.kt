package com.android.trade.data.implementations

import com.android.trade.data.local.datasource.RoomDataSource
import com.android.trade.data.local.entity.CoinListEntity
import com.android.trade.data.mapper.CoinDataMapper
import com.android.trade.domain.models.CoinInfo
import com.android.trade.domain.repositories.RoomRepository
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val roomDataSource: RoomDataSource,
    private val mapper: CoinDataMapper
) : RoomRepository {
    override suspend fun getAllCoin() = roomDataSource.getAllCoin()
            .mapNotNull(mapper::coinEntityToCoinInfo)
            .toMutableList()
    override suspend fun getCoin(market : String, code : String) = mapper.coinEntityToCoinInfo(roomDataSource.getCoin(market, code))
    override suspend fun deleteCoin(market : String, code : String) = roomDataSource.deleteCoin(market, code)
    override suspend fun insertCoin(coinInfo: CoinInfo) = roomDataSource.insertCoin(mapper.coinInfoToCoinEntity(coinInfo))

    override suspend fun insertCoinList(coinInfoList: List<CoinInfo>) = roomDataSource.insertCoinList(coinInfoList)
    override suspend fun getCoinList(market: String): List<CoinInfo?> = mapper.coinListEntityToCoinInfo(roomDataSource.getCoinList(market))
    override suspend fun deleteCoinList() = roomDataSource.deleteCoinList()
}