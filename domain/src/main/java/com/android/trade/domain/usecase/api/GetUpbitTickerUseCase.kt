package com.android.trade.domain.usecase.api

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.Ticker
import kotlinx.coroutines.flow.Flow

interface GetUpbitTickerUseCase {
    operator fun invoke(markets: String): Flow<ApiResult<Ticker>>
}