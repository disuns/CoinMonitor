package com.android.trade.data.remote.datasource

import com.android.trade.data.remote.response.BinanaceTickerResponse
import com.android.trade.data.remote.response.BinanceResponse
import com.android.trade.data.remote.response.BybitResponse
import com.android.trade.data.remote.response.BybitTickerResponse
import com.android.trade.data.remote.response.MarketResponse
import com.android.trade.data.remote.response.MarketTickerResponse
import com.android.trade.domain.ApiResult
import kotlinx.coroutines.flow.Flow
import retrofit2.http.QueryMap

interface CoinDataSource {
    fun fetchUpbitMarket() : Flow<ApiResult<MarketResponse>>
    fun fetchBithumbMarket() : Flow<ApiResult<MarketResponse>>
    fun fetchBinanceMarket() : Flow<ApiResult<BinanceResponse>>
    fun fetchBybitMarket() : Flow<ApiResult<BybitResponse>>
    fun fetchUpbitTicker(markets: String): Flow<ApiResult<MarketTickerResponse>>
    fun fetchBithumbTicker(markets: String): Flow<ApiResult<MarketTickerResponse>>
    fun fetchBinanceTicker(symbols: String): Flow<ApiResult<BinanaceTickerResponse>>
    fun fetchBybitTicker(symbol: String): Flow<ApiResult<BybitResponse>>
}