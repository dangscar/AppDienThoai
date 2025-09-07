package com.nlhd.user

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.logout.LogoutResponse
import com.nlhd.domain.entity.profile.ProfileResponse
import com.nlhd.domain.entity.profile.UpdateProfileReponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.authentication.AuthenticationUseCase
import com.nlhd.domain.usecase.authentication.UpdateProfile
import com.nlhd.keystore.KeyStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val authenticationUseCase: AuthenticationUseCase
): ViewModel() {

    private var _state = MutableStateFlow(
        EditProfileState(
            name = "",
            phone = "",
            address = "Chưa xác định"
        )
    )
    val state = _state.asStateFlow()

    private var _stateUpdateProfile = MutableStateFlow<UpdateProfileState>(UpdateProfileState.Idle)
    val stateUpdateProfile = _stateUpdateProfile.asStateFlow()

    fun setAddress(address: String) = _state.update { it.copy(address = address) }
    fun setPhone(phone: String) = _state.update { it.copy(phone = phone) }
    fun setName(name: String) = _state.update { it.copy(name = name) }

    fun fetchProfile(token: String) {
        viewModelScope.launch {
            authenticationUseCase.profile.invoke(token).let { result->
                when (result) {
                    is ResultWrapper.Failure -> {

                    }
                    is ResultWrapper.Success<*> -> {
                        val data = (result.value as ProfileResponse).user
                        _state.update {
                            it.copy(
                                name = data.name,
                                phone = data.phone ?: "",
                                address = data.address ?: "Chưa xác định"
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateProfile(token: String) = viewModelScope.launch {
        _stateUpdateProfile.update { UpdateProfileState.Loading }
        authenticationUseCase.updateProfile.invoke(
            token = token,
            updateProfileRequest = com.nlhd.domain.entity.profile.UpdateProfileRequest(
                address = state.value.address,
                name = state.value.name,
                phone = state.value.phone
            )
        ).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _stateUpdateProfile.update { UpdateProfileState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    val data = (result.value as UpdateProfileReponse).message
                    _stateUpdateProfile.update { UpdateProfileState.Success(data) }
                }
            }
        }
    }

}

data class EditProfileState(
    val name: String,
    val phone: String,
    val address: String
)

sealed class UpdateProfileState {
    data class Success(val message: String): UpdateProfileState()
    data class Error(val message: String): UpdateProfileState()
    object Loading: UpdateProfileState()
    object Idle: UpdateProfileState()
}


