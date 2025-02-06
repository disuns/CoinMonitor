package com.android.trade.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.android.trade.common.enum.MarketType
import com.android.trade.common.utils.logMessage
import com.android.trade.domain.ApiResult
import com.android.trade.domain.models.CoinInfo
import com.android.trade.domain.models.Ticker
import com.android.trade.presentation.mappers.CoinPresentationMapper
import com.android.trade.presentation.models.state.UpbitMarketViewState
import com.android.trade.presentation.usecasegroup.CoinMarketUseCaseGroup
import com.android.trade.presentation.usecasegroup.CoinTickerUseCaseGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val coinMarketUseCaseGroup: CoinMarketUseCaseGroup,
    private val coinTickerUseCaseGroup: CoinTickerUseCaseGroup,
    private val mapper : CoinPresentationMapper
): BaseViewModel<UpbitMarketViewState>(UpbitMarketViewState()) {
    fun fetchMarket(market : String): Flow<ApiResult<List<CoinInfo>>> {
        val flow = when(market){
            MarketType.UPBIT.id->coinMarketUseCaseGroup.getUpbitMarket()
            MarketType.BITHUMB.id->coinMarketUseCaseGroup.getBithumbMarket()
            MarketType.BINANCE.id->coinMarketUseCaseGroup.getBinanceMarket()
            else->coinMarketUseCaseGroup.getBybitMarket()
        }
        return mapper.domainToCoinInfo(flow, market)
    }

    fun fetchMarketSequentially(markets: List<String>, viewModel: RoomAndWebSocketViewModel) {
        viewModelScope.launch {
            viewModel.getAllCoin()

            val jobList = mutableListOf<Job>()

            for (market in markets) {
                val job = launch {
                    fetchMarket(market).collect { coinList ->
                        when (coinList) {
                            is ApiResult.Success -> {
                                viewModel.insertCoinList(coinList.value)
                            }
                            else -> {}
                        }
                    }
                }
                jobList.add(job)
            }

            jobList.joinAll()

            viewModel.sendAllMessage()
        }
    }

    fun fetchTicker(coinList : MutableList<CoinInfo>, viewModel: RoomAndWebSocketViewModel, onComplete : ()->Unit) {
        val result = mutableMapOf<String, MutableList<String>>()
        coinList.forEach { item ->
            val currentCodes = result.getOrPut(item.market) { mutableListOf() }
            currentCodes.add(item.code)
        }

        val upbitCodes = result[MarketType.UPBIT.id]?.joinToString(",") ?: ""
        val bithumbCodes = result[MarketType.BITHUMB.id]?.joinToString(",") ?: ""
        val binanceCodes = result[MarketType.BINANCE.id]
            ?.joinToString(separator = "\",\"") { it }
            ?.let { "[\"$it\"]" }
            ?: ""

        val bybitCodes = result[MarketType.BYBIT.id]?.toList() ?: emptyList()

        viewModelScope.launch {
            val upbitJob = async { fetchUpbitTicker(upbitCodes, coinList) }
            val bithumbJob = async { fetchBithumbTicker(bithumbCodes, coinList) }
            val binanceJob = async { fetchBinanceTicker(binanceCodes, coinList) }
            val bybitJobs = bybitCodes.map { code ->
                async { fetchBybitTicker(code, coinList) }
            }

            upbitJob.await()
            bithumbJob.await()
            binanceJob.await()
            bybitJobs.awaitAll()

            viewModel.updateList(coinList)
            onComplete()
        }
    }

    suspend fun fetchUpbitTicker(codes: String, coinList: MutableList<CoinInfo>) {
        if (codes.isNotEmpty()) {
            fetchTickers(MarketType.UPBIT.id, coinList){coinTickerUseCaseGroup.getUpbitTicker(codes)}
        }
    }

    suspend fun fetchBithumbTicker(codes: String, coinList: MutableList<CoinInfo>) {
        if (codes.isNotEmpty()) {
            fetchTickers(MarketType.BITHUMB.id, coinList){coinTickerUseCaseGroup.getBithumbTicker(codes)}
        }
    }

    suspend fun fetchBinanceTicker(codes: String, coinList: MutableList<CoinInfo>) {
        if (codes.isNotEmpty()) {
            fetchTickers(MarketType.BINANCE.id, coinList){coinTickerUseCaseGroup.getBinanceTicker(codes)}
        }
    }

    suspend fun fetchBybitTicker(code: String, coinList: MutableList<CoinInfo>) {
        fetchTickers(MarketType.BYBIT.id, coinList){coinTickerUseCaseGroup.getBybitTicker(code)}
    }

    suspend fun fetchTickers(
        market: String,
        coinList: MutableList<CoinInfo>,
        function: () -> Flow<ApiResult<Ticker>>
    ){
        function().collect { result ->
            when (result) {
                is ApiResult.Success -> {
                    result.value.forEach { value ->
                        updatePrices(coinList, market, value.code, mapper.domainToUIPrice(market, value.price))
                    }
                }
                else -> {}
            }
        }
    }

    private fun updatePrices(coinList: MutableList<CoinInfo>, marketId: String, code : String, newPrice: String) {
        coinList.find { it.market == marketId && it.code == code}?.let { coinInfo ->
            coinInfo.price = newPrice
        }
    }
}