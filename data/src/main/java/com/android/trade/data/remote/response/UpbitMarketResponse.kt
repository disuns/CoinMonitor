package com.android.trade.data.remote.response

class UpbitMarketResponse : ArrayList<UpbitMarketResponse.Item>(){
    data class Item(
        val market : String,
        val korean_name : String,
        val english_name : String
    )
}