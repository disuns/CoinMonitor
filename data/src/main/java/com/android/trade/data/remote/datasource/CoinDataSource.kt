package com.android.trade.data.remote.datasource

import com.android.trade.data.remote.response.UpbitMarketResponse
import com.android.trade.domain.ApiResult
import kotlinx.coroutines.flow.Flow
import retrofit2.http.QueryMap

interface CoinDataSource {
    fun fetchUpbitMarket(@QueryMap params: Map<String, String>) : Flow<ApiResult<UpbitMarketResponse>>
}