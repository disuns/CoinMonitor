package com.android.trade.domain.models

class UpbitMarket : ArrayList<UpbitMarket.Item>(){
    data class Item(
        val market : String,
        val korean_name : String,
        val english_name : String
    )
}
