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
    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state

    protected fun <T> fetchData(
        mapperAndUsecase: Flow<ApiResult<T>>,
        updateState: (currentState: S, result: ApiResult<T>) -> S
    ) {
        viewModelScope.launch {
            mapperAndUsecase.collect { result ->
                _state.value = updateState(_state.value, result)
            }
        }
    }
}