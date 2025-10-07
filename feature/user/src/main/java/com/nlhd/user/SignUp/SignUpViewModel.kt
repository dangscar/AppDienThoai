package com.nlhd.user.SignUp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.signup.SignUpRequest
import com.nlhd.domain.entity.signup.SignUpResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.authentication.AuthenticationUseCase
import com.nlhd.keystore.KeyStoreManager
import com.nlhd.user.Login.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authenticationUseCase: AuthenticationUseCase
): ViewModel() {
    private var _state = MutableStateFlow(SignUpAction(
        name = "Dang",
        email = "dangrt@gmail.com",
        password = "12345678",
        confirmPassword = "12345678"
    ))
    val state = _state.asStateFlow()

    private var _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState = _signUpState.asStateFlow()

    fun signUp() = viewModelScope.launch {
        _signUpState.update { SignUpState.Loading  }
        val signUpRequest = SignUpRequest(
            name = state.value.name,
            email = state.value.email,
            password = state.value.password,
            passwordConfirmation = state.value.confirmPassword
        )

        authenticationUseCase.signUp(signUpRequest).let { result ->
            when(result) {
                is ResultWrapper.Failure -> {
                    _signUpState.update { SignUpState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _signUpState.update { SignUpState.Success(result.value as SignUpResponse) }
                }
            }
        }
    }

    fun saveToken(context: Context,token: String) {
        viewModelScope.launch {
            KeyStoreManager.saveKeyStore(context, token)
        }
    }

    fun setName(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    fun setEmail(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun setPassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun setConfirmPassword(confirmPassword: String) {
        _state.value = _state.value.copy(confirmPassword = confirmPassword)
    }

}

data class SignUpAction(
    val name: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)

sealed class SignUpState {
    object Idle: SignUpState()
    object Loading: SignUpState()
    data class Success(val data: SignUpResponse): SignUpState()
    data class Error(val message: String): SignUpState()
}