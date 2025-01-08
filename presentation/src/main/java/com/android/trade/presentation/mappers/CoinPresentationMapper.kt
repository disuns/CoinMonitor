package com.android.trade.presentation.mappers

import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.CoinDomainModel
import com.android.trade.presentation.models.CoinUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object CoinPresentationMapper {
    fun domainToUI(coinFlow:Flow<ApiResult<CoinDomainModel>>): Flow<ApiResult<CoinUiModel>> {
        return coinFlow.map { coin->
            when(coin){
                is ApiResult.Success -> ApiResult.Success(CoinUiModel(id = coin.value.id))
                is ApiResult.Empty -> ApiResult.Empty
                is ApiResult.Loading -> ApiResult.Loading
                is ApiResult.Error -> ApiResult.Error(coin.code, coin.exception)
            }
        }
    }

    fun uiToDomain(coin: CoinUiModel): CoinDomainModel {
        return CoinDomainModel(
            id = coin.id
        )
    }
}