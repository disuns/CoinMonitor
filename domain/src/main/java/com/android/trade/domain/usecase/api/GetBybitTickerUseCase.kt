package com.android.trade.domain.usecase.api

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.Ticker
import kotlinx.coroutines.flow.Flow

interface GetBybitTickerUseCase {
    operator fun invoke(symbols: String): Flow<ApiResult<Ticker>>
}