package com.android.trade.data.manager

import com.android.trade.common.utils.logMessage
import com.android.trade.domain.WebSocketManager
import com.android.trade.domain.models.WebSocketData
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManagerImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
) : WebSocketManager {
    private val webSockets = mutableMapOf<String, WebSocket>()

    private var listener: ((WebSocketData?) -> Unit)? = null

    override fun connect(market: String): Boolean {
        if (webSockets.size > 3) {
            return false
        }
        if (webSockets.containsKey(market)) {
            return false
        }

        val url = when(market){
            "Upbit"->"wss://api.upbit.com/websocket/v1"
            "Bithumb"->"wss://pubwss.bithumb.com/pub/ws"
            "Bybit"->"wss://stream.bybit.com/v5/public/spot"
            else -> ""
        }

        if(url.isBlank()) return false

        val request = Request.Builder()
            .url(url)
            .build()
        val webSocket = okHttpClient.newWebSocket(request, webSocketListener)
        webSockets[market] = webSocket
        return true
    }

    override fun disconnect(market: String) {
        webSockets[market]?.close(1000, "Closing WebSocket")
        webSockets.remove(market)
    }

    override fun sendMessage(market: String, codes: List<String>) {
        val gson = Gson()
        val message = when(market){
            "Upbit"->{
                val jsonArray = JsonArray()
                codes.forEach { jsonArray.add(it) }

                gson.toJson(
                    listOf(
                        mapOf("ticket" to "unique_ticket"),
                        mapOf(
                            "type" to "ticker",
                            "codes" to jsonArray
                        )
                    )
                )
            }
            "Bithumb"->{
                val jsonArray = JsonArray()
                codes.forEach { code ->
                    val parts = code.split("-")
                    if (parts.size == 2) {
                        val newCode = "${parts[1]}_${parts[0]}"
                        jsonArray.add(newCode)
                    } else {
                        jsonArray.add(code)
                    }
                }

                gson.toJson(
                    mapOf(
                        "type" to "ticker",
                        "symbols" to jsonArray,
                        "tickTypes" to listOf("30M")
                    )
                )
            }
            "Binance"->{
                disconnect(market)
                val streams = codes.joinToString("/") { "${it.lowercase()}@trade" }
                val request = Request.Builder()
                    .url("wss://stream.binance.com:9443/stream?streams=$streams")
                    .build()
                val webSocket = okHttpClient.newWebSocket(request, webSocketListener)
                webSockets[market] = webSocket
                ""
            }
            "Bybit"->{
                val jsonArray = JsonArray()
                codes.forEach { code ->
                    jsonArray.add("tickers.$code")
                }

                gson.toJson(
                    mapOf(
                        "op" to "subscribe",
                        "args" to jsonArray
                    )
                )
            }
            else -> ""
        }

        logMessage(message)
//        logMessage(webSockets[market])
        if(message.isNotBlank())
            webSockets[market]?.send(message)
    }

    override fun disconnectAll() {
        webSockets.forEach { (_, webSocket) ->
            webSocket.close(1000, "Closing WebSocket")
        }
        webSockets.clear()
    }

    override fun setWebSocketListener(listener: (WebSocketData?) -> Unit) {
        this.listener = listener
    }

    val webSocketListener = object : WebSocketListener(){
        override fun onOpen(webSocket: WebSocket, response: Response) {
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
//            logMessage("message WebSocket : $webSocket , text : $text")
            when(webSockets.entries.firstOrNull { it.value == webSocket }?.key){
                "Bithumb"->{
                    logMessage(text)
                    listener?.invoke(getBithumbTradePrice(webSockets.entries.firstOrNull { it.value == webSocket }?.key, text))
                }
                "Binance"->{
                    listener?.invoke(getBinanceTradePrice(webSockets.entries.firstOrNull { it.value == webSocket }?.key, text))
                }
                "Bybit"->{
                    listener?.invoke(getBybitTradePrice(webSockets.entries.firstOrNull { it.value == webSocket }?.key, text))
                }
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
//            logMessage("message WebSocket : $webSocket , bytes : ${getUpbitTradePrice("Upbit", bytes)}")
            when(webSockets.entries.firstOrNull { it.value == webSocket }?.key){
                "Upbit"->{
                    listener?.invoke(getUpbitTradePrice(webSockets.entries.firstOrNull { it.value == webSocket }?.key, bytes))
                }
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {}

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            logMessage("failuere websocket")
        }
    }
}

private fun getUpbitTradePrice(market : String?, byteString: ByteString): WebSocketData? {
    return try {
        val jsonString = byteString.utf8()
        val jsonObject = JsonParser.parseString(jsonString).asJsonObject
         WebSocketData(market,jsonObject["code"]?.asString,jsonObject["trade_price"]?.asString)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun getBithumbTradePrice(market : String?, text: String): WebSocketData? {
    return try {
        val jsonObject = JsonParser.parseString(text).asJsonObject
        val data = jsonObject.getAsJsonObject("content")
        val symbol = data?.get("symbol")?.asString ?: ""
        val closePrice = data?.get("closePrice")?.asString ?: ""
        WebSocketData(market,symbol,closePrice)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun getBybitTradePrice(market : String?, text: String): WebSocketData? {
    return try {
        val jsonObject = JsonParser.parseString(text).asJsonObject
        val data = jsonObject.getAsJsonObject("data")
        WebSocketData(market,data["symbol"]?.asString,data["lastPrice"]?.asString)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun getBinanceTradePrice(market : String?, text: String): WebSocketData? {
    return try {
        val jsonObject = JsonParser.parseString(text).asJsonObject
        val data = jsonObject.getAsJsonObject("data")
        WebSocketData(market,data["s"]?.asString,data["p"]?.asString)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}