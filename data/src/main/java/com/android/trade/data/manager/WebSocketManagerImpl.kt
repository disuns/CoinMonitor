package com.android.trade.data.manager

import com.android.trade.common.utils.WEBSOCKET_BITHUMB
import com.android.trade.common.utils.WEBSOCKET_BYBIT
import com.android.trade.common.utils.WEBSOCKET_UPBIT
import com.android.trade.common.utils.logMessage
import com.android.trade.domain.WebSocketManager
import com.android.trade.domain.models.WebSocketData
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
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
            "Upbit"-> WEBSOCKET_UPBIT
            "Bithumb"-> WEBSOCKET_BITHUMB
            "Bybit"-> WEBSOCKET_BYBIT
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
                    val newCode = code.split("-").reversed().joinToString("_")
                    jsonArray.add(newCode)
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
        override fun onOpen(webSocket: WebSocket, response: Response) {}

        override fun onMessage(webSocket: WebSocket, text: String) {
            val key = webSockets.entries.firstOrNull { it.value == webSocket }?.key
            when(key){
                "Bithumb"->{
                    logMessage(getBithumbTradePrice(key, text))
                    listener?.invoke(getBithumbTradePrice(key, text))
                }
                "Binance"->{
                    logMessage(getBinanceTradePrice(key, text))
                    listener?.invoke(getBinanceTradePrice(key, text))
                }
                "Bybit"->{
                    logMessage(getBybitTradePrice(key, text))
                    listener?.invoke(getBybitTradePrice(key, text))
                }
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            val key = webSockets.entries.firstOrNull { it.value == webSocket }?.key
            when(key){
                "Upbit"->{
                    logMessage(getUpbitTradePrice(key, bytes))
                    listener?.invoke(getUpbitTradePrice(key, bytes))
                }
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {}

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {}
    }
}

private fun getTradePrice(market : String?, text: String?, byteString: ByteString?, onParsing : (JsonObject)->WebSocketData?): WebSocketData?{
    return try {
        val jsonString = text ?: byteString?.utf8()
        val jsonObject = JsonParser.parseString(jsonString).asJsonObject
        onParsing(jsonObject)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun getUpbitTradePrice(market : String?, byteString: ByteString): WebSocketData? {
    return getTradePrice(market, null, byteString ){ jsonObject ->
        WebSocketData(market,jsonObject["code"]?.asString,jsonObject["trade_price"]?.asString)
    }
}

private fun getBithumbTradePrice(market : String?, text: String): WebSocketData? {
    return getTradePrice(market, text, null ){ jsonObject ->
        val data = jsonObject.getAsJsonObject("content")
        val symbol = data?.get("symbol")?.asString ?: ""
        val closePrice = data?.get("closePrice")?.asString ?: ""
        val convertedSymbol = symbol.split("_").reversed().joinToString("-")

        WebSocketData(market,convertedSymbol,closePrice)
    }
}

private fun getBybitTradePrice(market : String?, text: String): WebSocketData? {
    return getTradePrice(market, text, null ){ jsonObject ->
        val data = jsonObject.getAsJsonObject("data")
        WebSocketData(market,data["symbol"]?.asString,data["lastPrice"]?.asString)
    }
}

private fun getBinanceTradePrice(market : String?, text: String): WebSocketData? {
    return getTradePrice(market, text, null ){ jsonObject ->
        val data = jsonObject.getAsJsonObject("data")
        WebSocketData(market,data["s"]?.asString,data["p"]?.asString)
    }
}