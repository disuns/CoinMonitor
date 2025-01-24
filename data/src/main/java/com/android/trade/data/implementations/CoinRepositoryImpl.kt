package com.android.trade.data.implementations

import com.android.trade.common.utils.logMessage
import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.Market
import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.data.mapper.CoinDataMapper
import com.android.trade.data.remote.datasource.CoinDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val coinDataSource: CoinDataSource,
    private val mapper: CoinDataMapper
) : CoinRepository {
    override fun fetchUpbitMarket()=mapper.responseToDomainCoin(coinDataSource.fetchUpbitMarket())
    override fun fetchBithumbMarket()=mapper.responseToDomainCoin(coinDataSource.fetchBithumbMarket())
    override fun fetchBinanceMarket()=mapper.responseToDomainBinanceCoin(coinDataSource.fetchBinanceMarket())
    override fun fetchBybitMarket()=mapper.responseToDomainBybitCoin(coinDataSource.fetchBybitMarket())
}