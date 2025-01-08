package com.android.trade.data.mapper

import com.android.trade.data.local.entity.CoinEntity
import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.CoinDomainModel
import com.android.trade.data.remote.response.CoinResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CoinDataMapper @Inject constructor() {
    fun responseToDomainCoin(coinFlow : Flow<ApiResult<CoinResponse>>): Flow<ApiResult<CoinDomainModel>> {
        return coinFlow.map { coin->
            when(coin){
                is ApiResult.Success -> ApiResult.Success(CoinDomainModel(id = coin.value.id))
                is ApiResult.Empty -> ApiResult.Empty
                is ApiResult.Loading -> ApiResult.Loading
                is ApiResult.Error -> ApiResult.Error(coin.code, coin.exception)
            }
        }
    }

    fun domainToResponse(coin: CoinDomainModel): CoinResponse {
        return CoinResponse(
            id = coin.id
        )
    }

    fun entityToDomain(coin: CoinEntity): CoinDomainModel {
        return CoinDomainModel(
            id = coin.id
        )
    }

    fun domainToEntity(coin: CoinDomainModel): CoinEntity {
        return CoinEntity(
            id = coin.id
        )
    }
}