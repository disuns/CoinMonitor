package com.android.trade.data.remote.network.service

import com.android.trade.data.remote.response.MarketResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface UpbitService {
    @GET("v1/market/all")
    suspend fun fetchUpbitMarket(@Query("isDetails") isDetails : Boolean = false): Response<MarketResponse>
}