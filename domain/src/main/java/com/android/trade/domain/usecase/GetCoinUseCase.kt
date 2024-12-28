package com.android.trade.domain.usecase

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.CoinDomainModel
import kotlinx.coroutines.flow.Flow

interface GetCoinUseCase {
    operator fun invoke() : Flow<ApiResult<CoinDomainModel>>
}