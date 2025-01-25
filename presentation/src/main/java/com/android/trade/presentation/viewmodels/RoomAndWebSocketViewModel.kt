package com.android.trade.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.trade.common.utils.logMessage
import com.android.trade.domain.WebSocketManager
import com.android.trade.domain.models.CoinInfo
import com.android.trade.domain.models.WebSocketData
import com.android.trade.domain.usecase.room.GetRoomAllCoinUseCase
import com.android.trade.domain.usecase.room.RoomDeleteCoinUseCase
import com.android.trade.domain.usecase.room.RoomInsertCoinUseCase
import com.android.trade.presentation.mappers.CoinPresentationMapper
import com.android.trade.presentation.models.state.RoomAndWebSocketState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomAndWebSocketViewModel @Inject constructor(
    private val roomInsertCoin: RoomInsertCoinUseCase,
    private val getRoomAllCoin: GetRoomAllCoinUseCase,
    private val roomDeleteCoin: RoomDeleteCoinUseCase,
    private val webSocketManager: WebSocketManager,
    private val mapper : CoinPresentationMapper
): BaseViewModel<RoomAndWebSocketState>(RoomAndWebSocketState()) {
    private val _messages = MutableLiveData<WebSocketData?>(null)
    val messages: LiveData<WebSocketData?> = _messages

    init {
        webSocketManager.setWebSocketListener { webSocketData ->
            viewModelScope.launch {
                webSocketData?.let {data ->
//                    val updatedList = _state.coinsListState.value.map { coin ->
//                        if (coin.market == data.market && coin.code == data.code && data.price != null) {
//                            val coinInfo = mapper.domainToUIPrice(data)
//                            coin.copy(price = coinInfo.price!!)
//                        } else {
//                            coin
//                        }
//                    }
//                    updateState(_state.coinsListState, updatedList)

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
            delay(100)
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
            val insertString = roomInsertCoin(coinInfo)
            if(insertString.isBlank())logMessage(coinInfo)
            else logMessage(insertString)

            getAllCoin()
//            val currentList = _state.coinsListState.value.toMutableList()
//            currentList.add(coinInfo)
//            updateState(_state.coinsListState, currentList)

            delay(100)
            val coinList = _state.coinsListState.value.filter { it.market == coinInfo.market }.map { it.code }.toMutableList()
            sendMessage(coinInfo.market, coinList)
        }
    }

    fun getAllCoin() {
        viewModelScope.launch {
            updateState(_state.coinsListState, getRoomAllCoin())
        }
    }

    fun deleteCoin(coinInfo: CoinInfo){
        viewModelScope.launch {
            roomDeleteCoin(coinInfo.market, coinInfo.code)
            delay(100)
            val currentList = _state.coinsListState.value.toMutableList()
            val itemRemoved = currentList.removeAll { it.market == coinInfo.market && it.code == coinInfo.code }

            if (itemRemoved) {
                updateState(_state.coinsListState, currentList)

                val exists = _state.coinsListState.value.any { it.market == coinInfo.market }
                _state.coinsListState.value.groupBy { it.market == coinInfo.market }
                if(!exists){
                    disconnectWebSocket(coinInfo.market)
                }else{
                    val coinList = _state.coinsListState.value.filter { it.market == coinInfo.market }.map { it.code }.toMutableList()
                    sendMessage(coinInfo.market, coinList)
                }
            }
        }
    }
}