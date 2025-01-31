package com.android.trade.data.remote.network.service

import retrofit2.http.Query
import com.android.trade.data.remote.response.MarketResponse
import com.android.trade.data.remote.response.MarketTickerResponse
import retrofit2.Response
import retrofit2.http.GET

interface BithumbService {
    @GET("v1/market/all")
    suspend fun fetchBithumbMarket(@Query("isDetails") isDetails : Boolean = false): Response<MarketResponse>
    @GET("v1/ticker")
    suspend fun fetchBithumbTicker(@Query("markets") markets : String): Response<MarketTickerResponse>
}