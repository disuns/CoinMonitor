package com.android.trade.domain.repositories

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.UpbitMarket
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun fetchUpbitMarket() : Flow<ApiResult<UpbitMarket>>
//    suspend fun localCoin() : CoinDomainModel
}