package com.android.trade.presentation.mappers

import android.annotation.SuppressLint
import android.content.Context
import com.android.trade.common.enum.MarketType
import com.android.trade.domain.ApiResult
import com.android.trade.domain.mappers.BaseMapper
import com.android.trade.domain.models.CoinInfo
import com.android.trade.domain.models.Market
import com.android.trade.domain.models.Ticker
import com.android.trade.domain.models.WebSocketData
import com.android.trade.presentation.R
import com.android.trade.presentation.models.MarketUiModel
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.text.DecimalFormat
import javax.inject.Inject

class CoinPresentationMapper @Inject constructor(val context: Context): BaseMapper() {
    fun domainToUIMarket(coinList: List<CoinInfo?>): MarketUiModel? {
        val firstItem = coinList.firstOrNull() ?: return null

        val items = coinList.mapNotNull { item ->
            item?.let {
                MarketUiModel.Item(
                    code = it.code,
                    name = item.coinName
                )
            }
        }.toMutableList()

        return MarketUiModel(market = firstItem.market, items = items)
    }

    @SuppressLint("StringFormatMatches")
    fun domainToUIPrice(webSocketData: WebSocketData): WebSocketData {
        val price = webSocketData.price?.let {
            when (webSocketData.market) {
                MarketType.UPBIT.id, MarketType.BITHUMB.id -> context.getString(R.string.won, it.formatWithComma())
                MarketType.BINANCE.id, MarketType.BYBIT.id -> context.getString(R.string.dollor, it.formatWithComma())
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


    @SuppressLint("StringFormatMatches")
    fun domainToUIPrice(market : String,price: String): String {
        return price.toDoubleOrNull()?.let {
            when (market) {
                MarketType.UPBIT.id, MarketType.BITHUMB.id -> context.getString(R.string.won, it)
                MarketType.BINANCE.id, MarketType.BYBIT.id -> context.getString(R.string.dollor, it)
                else -> ""
            }
        } ?: ""
    }

    fun domainToCoinInfo(flow: Flow<ApiResult<Market>>, market : String): Flow<ApiResult<List<CoinInfo>>> {
        return apiResultMapper(flow) {
            val itemFilter: (Market.Item) -> Boolean
            val coinNameMapper: (Market.Item) -> String
            when (market) {
                MarketType.UPBIT.id, MarketType.BITHUMB.id -> {
                    itemFilter = { it.market.contains("KRW") }
                    coinNameMapper = { "${it.englishName}(${it.koreanName})" }
                }
                MarketType.BINANCE.id, MarketType.BYBIT.id -> {
                    itemFilter = { it.market.endsWith("USDT") }
                    coinNameMapper = { it.englishName }
                }
                else -> {
                    itemFilter = { false }
                    coinNameMapper = { "" }
                }
            }
            val items = it.filter(itemFilter).map { item ->
                CoinInfo(
                    market = market,
                    code = item.market,
                    coinName = coinNameMapper(item)
                )
            }

            ApiResult.Success(items)
        }
    }
}