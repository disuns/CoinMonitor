package com.android.trade.data.remote.network.service

import com.android.trade.data.remote.response.BinanaceTickerResponse
import com.android.trade.data.remote.response.BinanceResponse
import com.android.trade.data.remote.response.MarketTickerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceService {
    @GET("api/v3/exchangeInfo")
    suspend fun fetchBinanceMarket(@Query("symbolStatus") symbolStatus : String = "TRADING"): Response<BinanceResponse>
    @GET("api/v3/ticker/24hr")
    suspend fun fetchBinanceTicker(@Query("symbols") symbols : String): Response<BinanaceTickerResponse>
}