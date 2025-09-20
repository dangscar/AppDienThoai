package com.nlhd.user.UploadAvatar

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.profile.ProfileResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.authentication.AuthenticationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.nlhd.domain.entity.Message.MessageResponse

class UploadAvatarViewModel(
    private val authenticationUseCase: AuthenticationUseCase
): ViewModel() {
    private var _state = MutableStateFlow(UploadAvatarAction(
        image = null
    ))
    val state = _state.asStateFlow()

    private var _uploadAvatarState = MutableStateFlow<UploadAvatarState>(UploadAvatarState.Idle)
    val uploadAvatarState = _uploadAvatarState.asStateFlow()

    fun setImage(image: Uri) {
        _state.update {
            it.copy(
                image = image
            )
        }
    }

    fun uploadAvatar(token: String, context: Context) = viewModelScope.launch {
        authenticationUseCase.uploadAvatar(token, context, state.value.image!!).let { result->
            when (result) {
                is ResultWrapper.Failure -> {
                    _uploadAvatarState.update { UploadAvatarState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _uploadAvatarState.update { UploadAvatarState.Success(result.value as MessageResponse) }
                }
            }
        }
    }

}

data class UploadAvatarAction(
    val image: Uri? = null
)

sealed class UploadAvatarState {
    object Idle: UploadAvatarState()
    object Loading: UploadAvatarState()
    data class Success(val data: MessageResponse): UploadAvatarState()
    data class Error(val message: String): UploadAvatarState()
}