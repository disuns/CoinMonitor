package com.android.trade.data.remote.datasource

import com.android.trade.domain.ApiResult
import com.android.trade.data.remote.network.safeFlow
import com.android.trade.data.remote.network.service.CoinService
import com.android.trade.data.remote.response.CoinResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RemoteDataSource {
    fun fetchCoin(id:Int) : Flow<ApiResult<CoinResponse>>
}

class RemoteDataSourceImpl @Inject constructor(
    private val coinService: CoinService
) : RemoteDataSource {
    override fun fetchCoin(id: Int) = safeFlow { coinService.getCoin() }
}