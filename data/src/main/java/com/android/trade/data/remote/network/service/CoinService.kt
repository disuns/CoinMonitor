package com.android.trade.data.remote.network.service

import com.android.trade.data.remote.response.CoinResponse
import retrofit2.Response
import retrofit2.http.GET

interface CoinService {
    @GET()
    suspend fun getCoin(): Response<CoinResponse>
}