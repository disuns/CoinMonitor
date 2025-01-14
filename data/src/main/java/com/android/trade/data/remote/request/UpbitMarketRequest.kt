package com.android.trade.data.remote.request

data class UpbitMarketRequest (
    val is_details: String
)
fun UpbitMarketRequest.toMap(): Map<String, String> {
    return mapOf(
        "is_details" to is_details
    )
}