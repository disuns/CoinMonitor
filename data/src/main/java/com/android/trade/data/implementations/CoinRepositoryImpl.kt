package com.android.trade.data.implementations

import com.android.trade.data.mapper.CoinDataMapper
import com.android.trade.data.remote.datasource.CoinDataSource
import com.android.trade.data.remote.response.BinanceResponse
import com.android.trade.domain.repositories.CoinRepository
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val coinDataSource: CoinDataSource,
    private val mapper: CoinDataMapper
) : CoinRepository {
    override fun fetchUpbitMarket()=mapper.responseToDomainCoin(coinDataSource.fetchUpbitMarket())
    override fun fetchBithumbMarket()=mapper.responseToDomainCoin(coinDataSource.fetchBithumbMarket())
    override fun fetchBinanceMarket()=mapper.responseToDomainBinanceCoin(coinDataSource.fetchBinanceMarket())
    override fun fetchBybitMarket()=mapper.responseToDomainBybitCoin(coinDataSource.fetchBybitMarket())

    override fun fetchUpbitTicker(markets : String)=mapper.responseToDomainTicker(coinDataSource.fetchUpbitTicker(markets))
    override fun fetchBithumbTicker(markets : String)=mapper.responseToDomainTicker(coinDataSource.fetchBithumbTicker(markets))
    override fun fetchBinanceTicker(symbols : String)=mapper.responseToDomainBinanceTicker(coinDataSource.fetchBinanceTicker(symbols))
    override fun fetchBybitTicker(symbol : String)=mapper.responseToDomainBybitTicker(coinDataSource.fetchBybitTicker(symbol))
}