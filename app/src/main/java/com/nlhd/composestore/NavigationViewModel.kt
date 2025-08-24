package com.nlhd.composestore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.admin.AdminProfileState
import com.nlhd.domain.entity.profile.ProfileResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.authentication.AuthenticationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationViewModel (
    private val authenticationUseCase: AuthenticationUseCase
): ViewModel() {

    private var _state: MutableStateFlow<NavigationState> = MutableStateFlow(NavigationState.Loading)
    val state: StateFlow<NavigationState> = _state.asStateFlow()

    fun getAdminProfile(token: String) {
        viewModelScope.launch {
            authenticationUseCase.profileAdmin(token).let { result->
                when (result) {
                    is ResultWrapper.Failure -> {
                        _state.update { NavigationState.Error(result.exception.message.toString()) }
                    }
                    is ResultWrapper.Success<*> -> {
                        _state.update { NavigationState.Success(result.value as ProfileResponse) }
                    }
                }
            }
        }
    }

}

sealed class NavigationState {
    object Loading: NavigationState()
    data class Success(val profileResponse: ProfileResponse): NavigationState()
    data class Error(val message: String): NavigationState()
}