package com.android.trade.data.implementations

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.UpbitMarket
import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.data.local.datasource.LocalDataSource
import com.android.trade.data.mapper.CoinDataMapper
import com.android.trade.data.remote.datasource.CoinDataSource
import com.android.trade.data.remote.request.UpbitMarketRequest
import com.android.trade.data.remote.request.toMap
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val coinDataSource: CoinDataSource,
    private val mapper: CoinDataMapper
) : CoinRepository {
    override fun fetchUpbitMarket(): Flow<ApiResult<UpbitMarket>>{
        val request = UpbitMarketRequest(is_details = "false")
        return mapper.responseToDomainCoin(coinDataSource.fetchUpbitMarket(request.toMap()))
    }

//    override suspend fun localCoin(): CoinDomainModel {
//        localDataSource
//        return CoinDomainModel(1)
//    }
}