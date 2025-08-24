package com.nlhd.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.profile.ProfileResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.authentication.AuthenticationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    private val authenticationUseCase: AuthenticationUseCase
): ViewModel() {

    private var _state = MutableStateFlow<UserState>(UserState.Loading)
    val state = _state.asStateFlow()

    fun fetchProfile(token: String) {
        viewModelScope.launch {
            authenticationUseCase.profile.invoke(token).let { result ->
                when (result) {
                    is ResultWrapper.Failure -> {
                        _state.update { UserState.Error(result.exception.message.toString()) }
                    }

                    is ResultWrapper.Success<*> -> {
                        _state.update { UserState.Success(result.value as ProfileResponse) }
                    }
                }
            }
        }

    }

}

sealed class UserState {
    object Loading : UserState()
    data class Success(val data: ProfileResponse) : UserState()
    data class Error(val message: String) : UserState()
}
