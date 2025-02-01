package com.android.trade.domain.repositories

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.Market
import com.android.trade.domain.models.Ticker
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun fetchUpbitMarket() : Flow<ApiResult<Market>>
    fun fetchBithumbMarket() : Flow<ApiResult<Market>>
    fun fetchBinanceMarket() : Flow<ApiResult<Market>>
    fun fetchBybitMarket() : Flow<ApiResult<Market>>
    fun fetchUpbitTicker(markets: String): Flow<ApiResult<Ticker>>
    fun fetchBithumbTicker(markets: String): Flow<ApiResult<Ticker>>
    fun fetchBinanceTicker(symbols: String): Flow<ApiResult<Ticker>>
    fun fetchBybitTicker(symbol: String): Flow<ApiResult<Ticker>>
}