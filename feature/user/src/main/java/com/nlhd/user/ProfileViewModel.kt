package com.nlhd.user

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.logout.LogoutResponse
import com.nlhd.domain.entity.profile.ProfileResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.authentication.AuthenticationUseCase
import com.nlhd.keystore.KeyStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authenticationUseCase: AuthenticationUseCase
): ViewModel() {

    private var _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state = _state.asStateFlow()

    private var _stateLogout = MutableStateFlow<LogoutState>(LogoutState.Loading)
    val stateLogout = _stateLogout.asStateFlow()

    fun fetchProfile(token: String) {
        viewModelScope.launch {
            authenticationUseCase.profile.invoke(token).let { result->
                when (result) {
                    is ResultWrapper.Failure -> {
                        _state.update { ProfileState.Error(result.exception.message.toString()) }
                    }
                    is ResultWrapper.Success<*> -> {
                        _state.update { ProfileState.Success(result.value as ProfileResponse) }
                    }
                }
            }
        }
    }

    fun logout(token: String) {
        viewModelScope.launch {
            authenticationUseCase.logout.invoke(token).let { result ->
                when (result) {
                    is ResultWrapper.Failure -> {
                        _stateLogout.update { LogoutState.Error(result.exception.message.toString()) }
                    }
                    is ResultWrapper.Success<*> -> {
                        _stateLogout.update { LogoutState.Success(result.value as LogoutResponse) }
                    }
                }
            }
        }
    }

    fun clearKeyStore(context: Context) {
        viewModelScope.launch {
            KeyStoreManager.clearKeyStore(context)
            _stateLogout.update { LogoutState.LogoutSuccess }
        }
    }


}

sealed class ProfileState {
    object Loading: ProfileState()
    data class Success(val data: ProfileResponse): ProfileState()
    data class Error(val message: String): ProfileState()
}

sealed class LogoutState {
    object Loading: LogoutState()
    data class Success(val data: LogoutResponse): LogoutState()
    object LogoutSuccess: LogoutState()
    data class Error(val message: String): LogoutState()
}

