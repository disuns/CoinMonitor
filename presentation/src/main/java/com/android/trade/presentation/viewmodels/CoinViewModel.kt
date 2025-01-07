package com.android.trade.presentation.viewmodels

import com.android.trade.domain.usecase.GetUpbitMarketUseCase
import com.android.trade.presentation.mappers.CoinPresentationMapper
import com.android.trade.presentation.models.state.UpbitMarketViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val getUpbitMarketUseCase: GetUpbitMarketUseCase,
    private val mapper : CoinPresentationMapper
): BaseViewModel<UpbitMarketViewState>(UpbitMarketViewState()) {
    fun fetchUpbitMarket() {
        fetchData(mapper.domainToUIUpbitMarket(getUpbitMarketUseCase())) { currentState, result->
            currentState.copy(upbitMarketState = result)
        }
    }
}