package com.nlhd.user.Login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.login.LoginResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.authentication.AuthenticationUseCase
import com.nlhd.keystore.KeyStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authenticationUseCase: AuthenticationUseCase
): ViewModel() {

    private var _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state = _state.asStateFlow()

    private var _email: MutableStateFlow<String> = MutableStateFlow("admin@gmail.com")
    val email = _email.asStateFlow()

    private var _password: MutableStateFlow<String> = MutableStateFlow("12345678")
    val password = _password.asStateFlow()

    fun setEmail(email: String) = _email.update { email }

    fun setPassword(password: String) = _password.update { password }

    fun login() {
        _state.update { LoginState.Loading }
        viewModelScope.launch {
            authenticationUseCase.login(email.value, password.value).let {result->
                when(result) {
                    is ResultWrapper.Failure -> {
                        _state.value = LoginState.Error(result.exception.message.toString())
                    }
                    is ResultWrapper.Success<*> -> {
                        _state.value = LoginState.Success(result.value as LoginResponse)
                    }
                }
            }
        }
    }

    fun saveToken(context: Context,token: String, role: String) {
        viewModelScope.launch {
            KeyStoreManager.saveKeyStore(context, token)
            _state.update {
                LoginState.SaveTokenSuccess(role)
            }
        }
    }

    fun setState(state: LoginState) = _state.update { state }

}

sealed class LoginState {
    object Idle: LoginState()
    data object Loading: LoginState()
    data class Success(val data: LoginResponse): LoginState()
    data class SaveTokenSuccess(val role: String): LoginState()
    data class Error(val message: String): LoginState()

}