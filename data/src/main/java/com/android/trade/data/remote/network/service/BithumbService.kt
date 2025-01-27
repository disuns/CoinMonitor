package com.android.trade.data.remote.network.service

import retrofit2.http.Query
import com.android.trade.data.remote.response.MarketResponse
import retrofit2.Response
import retrofit2.http.GET

interface BithumbService {
    @GET("v1/market/all")
    suspend fun fetchBithumbMarket(@Query("isDetails") isDetails : Boolean = false): Response<MarketResponse>
}