package com.android.trade.presentation.models

class UpbitMarketUiModel : ArrayList<UpbitMarketUiModel.Item>(){
    data class Item(
        val market : String,
        val name : String
    )
}