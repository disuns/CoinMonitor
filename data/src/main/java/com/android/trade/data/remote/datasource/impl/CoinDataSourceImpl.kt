package com.android.trade.data.remote.datasource.impl

import com.android.trade.data.remote.datasource.CoinDataSource
import com.android.trade.data.remote.network.safeFlow
import com.android.trade.data.remote.network.service.BinanceService
import com.android.trade.data.remote.network.service.BithumbService
import com.android.trade.data.remote.network.service.BybitService
import com.android.trade.data.remote.network.service.UpbitService
import com.android.trade.data.remote.response.MarketResponse
import com.android.trade.domain.ApiResult
import kotlinx.coroutines.flow.Flow
import retrofit2.http.QueryMap
import javax.inject.Inject

class CoinDataSourceImpl  @Inject constructor(
    private val upbitService: UpbitService,
    private val bithumbService: BithumbService,
    private val binanceService: BinanceService,
    private val bybitService: BybitService
) : CoinDataSource {
    override fun fetchUpbitMarket() = safeFlow { upbitService.fetchUpbitMarket() }
    override fun fetchBithumbMarket() = safeFlow { bithumbService.fetchBithumbMarket() }
    override fun fetchBinanceMarket() = safeFlow { binanceService.fetchBinanceMarket() }
    override fun fetchBybitMarket() = safeFlow { bybitService.fetchBybitMarket() }
}