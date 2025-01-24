package com.android.trade.domain.repositories

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.Market
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun fetchUpbitMarket() : Flow<ApiResult<Market>>
    fun fetchBithumbMarket() : Flow<ApiResult<Market>>
    fun fetchBinanceMarket() : Flow<ApiResult<Market>>
    fun fetchBybitMarket() : Flow<ApiResult<Market>>
}