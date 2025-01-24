package com.android.trade.presentation.mappers

import android.annotation.SuppressLint
import android.content.Context
import com.android.trade.domain.ApiResult
import com.android.trade.domain.mappers.BaseMapper
import com.android.trade.domain.models.Market
import com.android.trade.domain.models.WebSocketData
import com.android.trade.presentation.R
import com.android.trade.presentation.models.MarketUiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinPresentationMapper @Inject constructor(val context: Context): BaseMapper() {
    fun domainToUIMarket(flow: Flow<ApiResult<Market>>, market : String): Flow<ApiResult<MarketUiModel>> {
        return apiResultMapper(flow) {
            val model = MarketUiModel(
                market = market,
                items = ArrayList(
                    it.filter { item ->
                        when(market){
                            "Upbit","Bithumb"->item.market.contains("KRW")
                            "Binance","Bybit" -> item.market.endsWith("USDT")
                            else -> {false}
                        }

                    }.map { item ->
                        MarketUiModel.Item(
                            code = item.market,
                            name = item.english_name
                        )
                    }
                )
            )

            ApiResult.Success(model)
        }
    }

    @SuppressLint("StringFormatMatches")
    fun domainToUIPrice(webSocketData: WebSocketData?): WebSocketData? {
        val price = when(webSocketData?.market){
            "Upbit", "Bithumb"->{
                webSocketData.price?.toDoubleOrNull()?.let {
                    context.getString(R.string.won, it)
                } ?: ""
            }
            "Binance", "Bybit" -> {
                webSocketData.price?.toDoubleOrNull()?.let {
                    context.getString(R.string.dollor, it)
                } ?: ""
            }

            else -> ""
        }

        return webSocketData?.let {
            WebSocketData(
                market = webSocketData.market,
                code = webSocketData.code,
                price = price
            )
        }
    }
}