package com.android.trade.presentation.mappers

import com.android.trade.domain.ApiResult
import com.android.trade.domain.mappers.BaseMapper
import com.android.trade.domain.models.UpbitMarket
import com.android.trade.presentation.models.UpbitMarketUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CoinPresentationMapper @Inject constructor(): BaseMapper() {
    fun domainToUIUpbitMarket(flow: Flow<ApiResult<UpbitMarket>>): Flow<ApiResult<UpbitMarketUiModel>> {
        return apiResultMapper(flow) {
            val list = UpbitMarketUiModel().apply {
                addAll(
                    it.map { item ->
                        UpbitMarketUiModel.Item(
                            market = item.market,
                            name = item.english_name
                        )
                    }
                )
            }

            ApiResult.Success(list)
        }
    }
}