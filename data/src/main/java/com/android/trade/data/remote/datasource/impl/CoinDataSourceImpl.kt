package com.android.trade.data.remote.datasource.impl

import com.android.trade.data.remote.datasource.CoinDataSource
import com.android.trade.data.remote.network.safeFlow
import com.android.trade.data.remote.network.service.UpbitService
import retrofit2.http.QueryMap
import javax.inject.Inject

class CoinDataSourceImpl  @Inject constructor(
    private val upbitService: UpbitService
) : CoinDataSource {
    override fun fetchUpbitMarket(@QueryMap params: Map<String, String>) = safeFlow { upbitService.fetchUpbitMarket(params) }
}