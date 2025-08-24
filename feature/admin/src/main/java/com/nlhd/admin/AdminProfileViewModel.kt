package com.nlhd.admin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.logout.LogoutResponse
import com.nlhd.domain.entity.profile.ProfileResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.authentication.AuthenticationUseCase
import com.nlhd.keystore.KeyStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminProfileViewModel(
    private val authenticationUseCase: AuthenticationUseCase
): ViewModel() {

    private val _state: MutableStateFlow<AdminProfileState> = MutableStateFlow(AdminProfileState.Loading)
    val state: StateFlow<AdminProfileState> = _state.asStateFlow()

    private var _stateLogout = MutableStateFlow<AdminLogoutState>(AdminLogoutState.Loading)
    val stateLogout = _stateLogout.asStateFlow()



    fun getAdminProfile(token: String) {
        viewModelScope.launch {
            authenticationUseCase.profileAdmin(token).let { result->
                when (result) {
                    is ResultWrapper.Failure -> {
                        _state.update { AdminProfileState.Error(result.exception.message.toString()) }
                    }
                    is ResultWrapper.Success<*> -> {
                        _state.update { AdminProfileState.Success(result.value as ProfileResponse) }
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
                        _stateLogout.update { AdminLogoutState.Error(result.exception.message.toString()) }
                    }
                    is ResultWrapper.Success<*> -> {
                        _stateLogout.update { AdminLogoutState.Success(result.value as LogoutResponse) }
                    }
                }
            }
        }
    }

    fun clearKeyStore(context: Context) {
        viewModelScope.launch {
            KeyStoreManager.clearKeyStore(context)
            _stateLogout.update { AdminLogoutState.LogoutSuccess }
        }
    }

}

sealed class AdminProfileState {
    object Loading: AdminProfileState()
    data class Success(val data: ProfileResponse): AdminProfileState()
    data class Error(val message: String): AdminProfileState()
}

sealed class AdminLogoutState {
    object Loading: AdminLogoutState()
    data class Success(val data: LogoutResponse): AdminLogoutState()
    data class Error(val message: String): AdminLogoutState()
    object LogoutSuccess: AdminLogoutState()
}