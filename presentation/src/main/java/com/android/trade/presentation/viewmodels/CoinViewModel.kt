package com.android.trade.presentation.viewmodels

import com.android.trade.common.utils.logMessage
import com.android.trade.domain.ApiResult
import com.android.trade.domain.usecase.GetBinanceMarketUseCase
import com.android.trade.domain.usecase.GetBithumbMarketUseCase
import com.android.trade.domain.usecase.GetBybitMarketUseCase
import com.android.trade.domain.usecase.GetUpbitMarketUseCase
import com.android.trade.presentation.mappers.CoinPresentationMapper
import com.android.trade.presentation.models.state.UpbitMarketViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val getUpbitMarketUseCase: GetUpbitMarketUseCase,
    private val getBithumbMarketUseCase: GetBithumbMarketUseCase,
    private val getBinanceMarketUseCase: GetBinanceMarketUseCase,
    private val getBybitMarketUseCase: GetBybitMarketUseCase,
    private val mapper : CoinPresentationMapper
): BaseViewModel<UpbitMarketViewState>(UpbitMarketViewState()) {

    fun fetchMarket(market : String) {
        when(market){
            "Upbit"->fetchData(mapper.domainToUIMarket(getUpbitMarketUseCase(), market), _state.marketState )
            "Bithumb"->fetchData(mapper.domainToUIMarket(getBithumbMarketUseCase(), market), _state.marketState )
            "Binance"->fetchData(mapper.domainToUIMarket(getBinanceMarketUseCase(), market), _state.marketState )
            "Bybit"->fetchData(mapper.domainToUIMarket(getBybitMarketUseCase(), market), _state.marketState )
        }
    }

    fun resetState(){
        updateState(_state.marketState, ApiResult.Empty)
    }
}