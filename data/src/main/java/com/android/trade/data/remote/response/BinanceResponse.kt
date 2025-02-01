package com.android.trade.data.remote.response

data class BinanceResponse (
    val exchangeFilters: List<Any>,
    val rateLimits: List<RateLimit>,
    val serverTime: Long,
    val symbols: List<Symbol>,
    val timezone: String
) {
    data class RateLimit(
        val interval: String,
        val intervalNum: Int,
        val limit: Int,
        val rateLimitType: String
    )

    data class Symbol(
        val allowTrailingStop: Boolean,
        val allowedSelfTradePreventionModes: List<String>,
        val baseAsset: String,
        val baseAssetPrecision: Int,
        val baseCommissionPrecision: Int,
        val cancelReplaceAllowed: Boolean,
        val defaultSelfTradePreventionMode: String,
        val filters: List<Filter>,
        val icebergAllowed: Boolean,
        val isMarginTradingAllowed: Boolean,
        val isSpotTradingAllowed: Boolean,
        val ocoAllowed: Boolean,
        val orderTypes: List<String>,
        val otoAllowed: Boolean,
        val permissionSets: List<List<String>>,
        val permissions: List<Any>,
        val quoteAsset: String,
        val quoteAssetPrecision: Int,
        val quoteCommissionPrecision: Int,
        val quoteOrderQtyMarketAllowed: Boolean,
        val quotePrecision: Int,
        val status: String,
        val symbol: String
    ) {
        data class Filter(
            val applyMaxToMarket: Boolean,
            val applyMinToMarket: Boolean,
            val askMultiplierDown: String,
            val askMultiplierUp: String,
            val avgPriceMins: Int,
            val bidMultiplierDown: String,
            val bidMultiplierUp: String,
            val filterType: String,
            val limit: Int,
            val maxNotional: String,
            val maxNumAlgoOrders: Int,
            val maxNumOrders: Int,
            val maxPrice: String,
            val maxQty: String,
            val maxTrailingAboveDelta: Int,
            val maxTrailingBelowDelta: Int,
            val minNotional: String,
            val minPrice: String,
            val minQty: String,
            val minTrailingAboveDelta: Int,
            val minTrailingBelowDelta: Int,
            val stepSize: String,
            val tickSize: String
        )
    }
}