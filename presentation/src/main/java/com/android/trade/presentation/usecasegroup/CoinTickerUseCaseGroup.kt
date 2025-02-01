package com.android.trade.presentation.usecasegroup

import com.android.trade.domain.usecase.api.GetBinanceTickerUseCase
import com.android.trade.domain.usecase.api.GetBithumbTickerUseCase
import com.android.trade.domain.usecase.api.GetBybitTickerUseCase
import com.android.trade.domain.usecase.api.GetUpbitTickerUseCase
import javax.inject.Inject

class CoinTickerUseCaseGroup @Inject constructor(
    val getUpbitTicker: GetUpbitTickerUseCase,
    val getBithumbTicker: GetBithumbTickerUseCase,
    val getBinanceTicker: GetBinanceTickerUseCase,
    val getBybitTicker: GetBybitTickerUseCase
)