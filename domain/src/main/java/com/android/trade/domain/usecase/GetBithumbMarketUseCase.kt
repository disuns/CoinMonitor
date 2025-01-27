package com.android.trade.domain.usecase

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.Market
import kotlinx.coroutines.flow.Flow

interface GetBithumbMarketUseCase {
    operator fun invoke() : Flow<ApiResult<Market>>
}