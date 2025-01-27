package com.android.trade.presentation.models.state

import com.android.trade.domain.ApiResult
import com.android.trade.presentation.models.MarketUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class UpbitMarketViewState(
    var marketState : StateFlow<ApiResult<MarketUiModel>> = MutableStateFlow(ApiResult.Loading),
): BaseViewState {
}
