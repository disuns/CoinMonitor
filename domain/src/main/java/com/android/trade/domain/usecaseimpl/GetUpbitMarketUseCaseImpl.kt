package com.android.trade.domain.usecaseimpl

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.UpbitMarket
import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.GetUpbitMarketUseCase
import kotlinx.coroutines.flow.Flow

class GetUpbitMarketUseCaseImpl(
    private val coinRepository: CoinRepository
) : GetUpbitMarketUseCase {
    override fun invoke(): Flow<ApiResult<UpbitMarket>> {
        return coinRepository.fetchUpbitMarket()
    }
}