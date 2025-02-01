package com.android.trade.data.remote.response

class BinanaceTickerResponse : ArrayList<BinanaceTickerResponse.Item>(){
    data class Item(
        val askPrice: String,
        val askQty: String,
        val bidPrice: String,
        val bidQty: String,
        val closeTime: Long,
        val count: Int,
        val firstId: Long,
        val highPrice: String,
        val lastId: Long,
        val lastPrice: String,
        val lastQty: String,
        val lowPrice: String,
        val openPrice: String,
        val openTime: Long,
        val prevClosePrice: String,
        val priceChange: String,
        val priceChangePercent: String,
        val quoteVolume: String,
        val symbol: String,
        val volume: String,
        val weightedAvgPrice: String
    )
}