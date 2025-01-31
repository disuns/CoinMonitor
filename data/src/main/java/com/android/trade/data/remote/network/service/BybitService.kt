package com.android.trade.data.remote.network.service

import com.android.trade.data.remote.response.BybitResponse
import com.android.trade.data.remote.response.BybitTickerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BybitService {
    @GET("v5/market/tickers")
    suspend fun fetchBybitMarket(@Query("symbol") symbol : String = "", @Query("category") category : String = "spot"): Response<BybitResponse>
}