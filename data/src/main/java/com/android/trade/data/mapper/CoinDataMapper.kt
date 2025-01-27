package com.android.trade.data.mapper

import com.android.trade.data.local.entity.CoinEntity
import com.android.trade.data.remote.response.BinanceResponse
import com.android.trade.data.remote.response.BybitResponse
import com.android.trade.data.remote.response.MarketResponse
import com.android.trade.domain.ApiResult
import com.android.trade.domain.mappers.BaseMapper
import com.android.trade.domain.models.CoinInfo
import com.android.trade.domain.models.Market
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinDataMapper @Inject constructor() : BaseMapper() {
    fun coinInfoToCoinEntity(coinInfo: CoinInfo): CoinEntity {
        return CoinEntity(
            market = coinInfo.market,
            code = coinInfo.code,
            coinName = coinInfo.coinName
        )
    }

    fun coinEntityToCoinInfo(coinEntity: CoinEntity?): CoinInfo? {
        return coinEntity?.let {
            CoinInfo(
                market = it.market,
                code = it.code,
                coinName = it.coinName
            )
        }
    }

    private fun <T> responseToDomainCodeBase(
        response: Flow<ApiResult<T>>,
        mapItem: (T) -> List<Market.Item>
    ): Flow<ApiResult<Market>> {
        return apiResultMapper(response) { apiResult ->
            val list = Market().apply {
                addAll(mapItem(apiResult))
            }
            ApiResult.Success(list)
        }
    }

    fun responseToDomainCoin(response: Flow<ApiResult<MarketResponse>>): Flow<ApiResult<Market>> {
        return responseToDomainCodeBase(response){
            it.map { item ->
                Market.Item(
                    market = item.market,
                    englishName = item.english_name,
                    koreanName = item.korean_name
                )
            }
        }
    }

    fun responseToDomainBinanceCoin(response: Flow<ApiResult<BinanceResponse>>): Flow<ApiResult<Market>> {
        return responseToDomainCodeBase(response) {
            it.symbols.map { item ->
                Market.Item(
                    market = item.symbol,
                    englishName = item.baseAsset,
                    koreanName = ""
                )
            }
        }
    }

    fun responseToDomainBybitCoin(response: Flow<ApiResult<BybitResponse>>): Flow<ApiResult<Market>> {
        return responseToDomainCodeBase(response) {
            it.result.list.sortedByDescending { it.lastPrice.toDouble() }.map { item ->
                Market.Item(
                    market = item.symbol,
                    englishName = item.symbol,
                    koreanName = ""
                )
            }
        }
    }
}