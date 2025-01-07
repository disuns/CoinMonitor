package com.android.trade.domain.usecase

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.UpbitMarket
import kotlinx.coroutines.flow.Flow

interface GetUpbitMarketUseCase {
    operator fun invoke() : Flow<ApiResult<UpbitMarket>>
}