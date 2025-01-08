package com.android.trade.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.trade.domain.ApiResult
import com.android.trade.presentation.mappers.CoinPresentationMapper
import com.android.trade.presentation.models.CoinUiModel
import com.android.trade.domain.usecase.GetCoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(private val getCoinUseCase: GetCoinUseCase) : ViewModel() {
    val state: StateFlow<ApiResult<CoinUiModel>> = CoinPresentationMapper
        .domainToUI(getCoinUseCase())
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.Lazily,
            initialValue = ApiResult.Loading
        )
}