package com.android.trade.presentation.models.state

import com.android.trade.domain.models.CoinInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class RoomAndWebSocketState(
    var coinsListState : StateFlow<List<CoinInfo>> = MutableStateFlow(emptyList())
): BaseViewState {
}
