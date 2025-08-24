package com.nlhd.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.dashboard.DashboardResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.dashboard.DashboardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val useCase: DashboardUseCase
): ViewModel() {
    private var _state: MutableStateFlow<DashboardState> = MutableStateFlow<DashboardState>(DashboardState.Pending)
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    fun getDashboard(token: String) = viewModelScope.launch {
        _state.update { DashboardState.Loading }
        useCase.getDashboard(token).let { result->
            when (result) {
                is ResultWrapper.Failure -> {
                    _state.value = DashboardState.Error(result.exception.message.toString())
                }
                is ResultWrapper.Success<*> -> {
                    _state.value = DashboardState.Success(result.value as DashboardResponse)
                }
            }
        }
    }
}

sealed class DashboardState {
    object Loading: DashboardState()
    object Pending: DashboardState()
    data class Success(val data: DashboardResponse): DashboardState()
    data class Error(val message: String): DashboardState()
}