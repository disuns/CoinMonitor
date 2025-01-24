package com.android.trade.domain.models

class Market : ArrayList<Market.Item>(){
    data class Item(
        val market : String,
        val korean_name : String,
        val english_name : String
    )
}
