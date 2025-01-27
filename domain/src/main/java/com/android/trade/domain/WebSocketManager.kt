package com.android.trade.domain

import com.android.trade.domain.models.WebSocketData

interface WebSocketManager {
    fun connect(market: String): Boolean
    fun disconnect(market: String)
    fun sendMessage(market: String, codes: List<String>)
    fun disconnectAll()
    fun setWebSocketListener(listener: (WebSocketData?) -> Unit)
}