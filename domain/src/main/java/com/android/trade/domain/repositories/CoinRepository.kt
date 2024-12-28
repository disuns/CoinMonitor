package com.android.trade.domain.repositories

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.CoinDomainModel
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    fun fetchCoin() : Flow<ApiResult<CoinDomainModel>>
    suspend fun localCoin() : CoinDomainModel
}