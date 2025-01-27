package com.android.trade.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.trade.presentation.models.state.BaseViewState
import com.android.trade.domain.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BaseViewModel<S: BaseViewState>(initialState: S) : ViewModel()  {
    protected var _state = initialState
    val state: S = _state

    protected fun <T> fetchData(
        mapperAndUsecase: Flow<ApiResult<T>>,
        flow: StateFlow<ApiResult<T>>
    ) {
        viewModelScope.launch {
            mapperAndUsecase.collect { result ->
                updateState(flow, result)
            }
        }
    }

    fun <T> updateState(stateFlow: StateFlow<T>, newValue: T) {
        (stateFlow as MutableStateFlow).value = newValue
    }
}