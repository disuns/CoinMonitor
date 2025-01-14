package com.android.trade.presentation.models.state

import com.android.trade.domain.ApiResult
import com.android.trade.presentation.models.UpbitMarketUiModel

data class UpbitMarketViewState(
    val upbitMarketState : ApiResult<UpbitMarketUiModel> = ApiResult.Loading
): BaseViewState {
}
