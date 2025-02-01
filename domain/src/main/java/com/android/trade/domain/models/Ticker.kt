package com.android.trade.domain.models

class Ticker : ArrayList<Ticker.Item>(){
    data class Item(
        val code : String,
        val price : String
    )
}