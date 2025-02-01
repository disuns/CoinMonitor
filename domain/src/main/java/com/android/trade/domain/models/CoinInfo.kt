package com.android.trade.domain.models

data class CoinInfo(
    val market: String,
    val code: String,
    val coinName: String,
    var price : String = ""
)
