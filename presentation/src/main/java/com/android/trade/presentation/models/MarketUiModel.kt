package com.android.trade.presentation.models

data class MarketUiModel(
    val market : String,
    val items : MutableList<Item>
) {
    data class Item(
        val code : String,
        val name : String
    )
}