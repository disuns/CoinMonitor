package com.android.trade.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.trade.common.utils.logMessage
import com.android.trade.domain.WebSocketManager
import com.android.trade.domain.models.CoinInfo
import com.android.trade.domain.models.WebSocketData
import com.android.trade.presentation.mappers.CoinPresentationMapper
import com.android.trade.presentation.models.state.RoomAndWebSocketState
import com.android.trade.presentation.usecasegroup.RoomCoinListUseCaseGroup
import com.android.trade.presentation.usecasegroup.RoomCoinUseCaseGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomAndWebSocketViewModel @Inject constructor(
    private val roomCoinGroup: RoomCoinUseCaseGroup,
    private val roomCoinListGroup: RoomCoinListUseCaseGroup,
    private val webSocketManager: WebSocketManager,
    private val mapper : CoinPresentationMapper
): BaseViewModel<RoomAndWebSocketState>(RoomAndWebSocketState()) {
    private val _messages = MutableLiveData<WebSocketData?>(null)
    val messages: LiveData<WebSocketData?> = _messages

    init {
        webSocketManager.setWebSocketListener { webSocketData ->
            viewModelScope.launch {
                webSocketData?.let {data ->
                    _messages.postValue(mapper.domainToUIPrice(webSocketData))
                }
            }
        }
    }

    fun connectWebSocket(market: String) {
        viewModelScope.launch {
            val success = webSocketManager.connect(market)
            if (!success) {
                "$market 이미 연결중 또는 연결개수 초과"
            }
        }
    }

    fun disconnectWebSocket(market: String) {
        viewModelScope.launch {
            webSocketManager.disconnect(market)
        }
    }

    fun sendAllMessage(){
        viewModelScope.launch {
            val groupedByMarket = _state.coinsListState.value.groupBy { it.market }
            groupedByMarket.forEach { (market, coinInfos) ->
                val codes = coinInfos.map { it.code }.toMutableList()
                sendMessage(market, codes)
            }
        }
    }


    fun sendMessage(market: String, codes: MutableList<String>) {
        viewModelScope.launch {
            webSocketManager.sendMessage(market,codes)
        }
    }

    fun disconnectAll(){
        viewModelScope.launch {
            webSocketManager.disconnectAll()
        }
    }
    fun insertCoin(coinInfo: CoinInfo){
        viewModelScope.launch {
            roomCoinGroup.insertCoin(coinInfo)

            val updatedList = _state.coinsListState.value.toMutableList().apply {
                add(coinInfo)
            }
            updateState(_state.coinsListState, updatedList)

            val coinList = _state.coinsListState.value.filter { it.market == coinInfo.market }.map { it.code }.toMutableList()

            logMessage(coinList)
            sendMessage(coinInfo.market, coinList)
        }
    }

    fun getAllCoin() {
        viewModelScope.launch {
            val newList = roomCoinGroup.getAllCoin()
            val oldList = _state.coinsListState.value.toMutableList()

            val oldSet = oldList.map { it.market to it.code }.toSet()
            val newSet = newList.map { it.market to it.code }.toSet()

            val toRemove = oldList.filter { (it.market to it.code) !in newSet }

            val toAdd = newList.filter { (it.market to it.code) !in oldSet }

            if (toRemove.isNotEmpty() || toAdd.isNotEmpty()) {
                oldList.removeAll(toRemove)
                oldList.addAll(toAdd)
                updateState(_state.coinsListState, oldList)
            }
        }
    }

    fun deleteCoin(coinInfo: CoinInfo){
        viewModelScope.launch {
            roomCoinGroup.deleteCoin(coinInfo.market, coinInfo.code)

            val updatedList = _state.coinsListState.value.filterNot {
                it.market == coinInfo.market && it.code == coinInfo.code
            }

            if (updatedList.size != _state.coinsListState.value.size) {
                updateState(_state.coinsListState, updatedList)

                val updatedMatching = updatedList.filter { it.market == coinInfo.market }

                if (updatedMatching.isEmpty()) {
                    disconnectWebSocket(coinInfo.market)
                } else {
                    sendMessage(coinInfo.market, updatedMatching.map { it.code }.toMutableList())
                }
            }
        }
    }

    fun insertCoinList(coinList : List<CoinInfo>){
        viewModelScope.launch {
            roomCoinListGroup.insertCoinList(coinList)
        }
    }

    suspend fun getCoinList(market:String) = mapper.domainToUIMarket(roomCoinListGroup.getCoinList(market))

    fun deleteCoinList(){
        viewModelScope.launch {
            roomCoinListGroup.deleteCoinList()
        }
    }

    fun updateList(coinList: List<CoinInfo>) {
        updateState(_state.coinsListState, coinList)
    }
}