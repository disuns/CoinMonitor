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
import java.math.BigDecimal
import java.text.DecimalFormat
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
    fun domainToUIPrice(webSocketData: WebSocketData): WebSocketData {

        val price = webSocketData.price?.let {
            when (webSocketData.market) {
                "Upbit", "Bithumb" -> context.getString(R.string.won, it.formatWithComma())
                "Binance", "Bybit" -> context.getString(R.string.dollor, it.formatWithComma())
                else -> ""
            }
        } ?: ""

        return WebSocketData(
            market = webSocketData.market,
            code = webSocketData.code,
            price = price
        )
    }

    private fun String.formatWithComma(): String {
        if (this.isBlank()) return "0"

        val bigDecimal = BigDecimal(this) // 문자열을 BigDecimal로 변환
        val integerPart = DecimalFormat("#,###").format(bigDecimal.toBigInteger()) // 정수 부분 쉼표 추가
        val decimalPart = bigDecimal.stripTrailingZeros().toPlainString().split(".").getOrNull(1) // 불필요한 0 제거

        return if (decimalPart != null) "$integerPart.$decimalPart" else integerPart // 소수점 이하가 있으면 유지
    }

}