package com.android.trade.presentation.usecasegroup

import com.android.trade.domain.usecase.api.GetBinanceMarketUseCase
import com.android.trade.domain.usecase.api.GetBinanceTickerUseCase
import com.android.trade.domain.usecase.api.GetBithumbMarketUseCase
import com.android.trade.domain.usecase.api.GetBithumbTickerUseCase
import com.android.trade.domain.usecase.api.GetBybitMarketUseCase
import com.android.trade.domain.usecase.api.GetBybitTickerUseCase
import com.android.trade.domain.usecase.api.GetUpbitMarketUseCase
import com.android.trade.domain.usecase.api.GetUpbitTickerUseCase
import javax.inject.Inject

class CoinMarketUseCaseGroup @Inject constructor(
    val getUpbitMarket: GetUpbitMarketUseCase,
    val getBithumbMarket: GetBithumbMarketUseCase,
    val getBinanceMarket: GetBinanceMarketUseCase,
    val getBybitMarket: GetBybitMarketUseCase
)