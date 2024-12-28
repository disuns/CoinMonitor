package com.android.trade.domain.usecaseimpl

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.CoinDomainModel
import com.android.trade.domain.repositories.CoinRepository
import com.android.trade.domain.usecase.GetCoinUseCase
import kotlinx.coroutines.flow.Flow

class GetCoinUseCaseImpl(
    private val coinRepository: CoinRepository
) : GetCoinUseCase {
    override fun invoke(): Flow<ApiResult<CoinDomainModel>> {
        return coinRepository.fetchCoin()
    }
}