package com.android.trade.data.mapper

import com.android.trade.data.remote.response.UpbitMarketResponse
import com.android.trade.domain.ApiResult
import com.android.trade.domain.mappers.BaseMapper
import com.android.trade.domain.models.UpbitMarket
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinDataMapper @Inject constructor() : BaseMapper() {
    fun responseToDomainCoin(response: Flow<ApiResult<UpbitMarketResponse>>): Flow<ApiResult<UpbitMarket>> {
        return apiResultMapper(response) {
            val list = UpbitMarket().apply {
                addAll(
                    it.map { item ->
                        UpbitMarket.Item(
                            market = item.market,
                            korean_name = item.korean_name,
                            english_name = item.english_name
                        )
                    }
                )
            }

            ApiResult.Success(list)
        }
    }
}