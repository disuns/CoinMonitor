package com.android.trade.data.remote.response

data class BybitResponse(
    val result: Result,
    val retCode: Int,
    val retExtInfo: RetExtInfo,
    val retMsg: String,
    val time: Long
) {
    data class Result(
        val category: String,
        val list: List<Item>
    ) {
        data class Item(
            val ask1Price: String,
            val ask1Size: String,
            val bid1Price: String,
            val bid1Size: String,
            val highPrice24h: String,
            val lastPrice: String,
            val lowPrice24h: String,
            val prevPrice24h: String,
            val price24hPcnt: String,
            val symbol: String,
            val turnover24h: String,
            val usdIndexPrice: String,
            val volume24h: String
        )
    }

    class RetExtInfo
}