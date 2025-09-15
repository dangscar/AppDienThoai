package com.nlhd.shortvideo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nlhd.domain.entity.shortVideo.ProfileShortVideo.Info.InfoProfileResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.shortvideo.ShortVideoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileShortVideoViewModel(
    private val shortVideoUseCase: ShortVideoUseCase
): ViewModel() {

    private var _state = MutableStateFlow<ProfileShortVideoState>(ProfileShortVideoState.Loading)
    val state = _state.asStateFlow()

    fun getInfoProfile(token: String, videoId: Int) = viewModelScope.launch {
        shortVideoUseCase.getInfoProfile(token, videoId).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _state.update { ProfileShortVideoState.Error(result.exception.message.toString() ) }
                }
                is ResultWrapper.Success<*> -> {
                    _state.update { ProfileShortVideoState.Success(result.value as InfoProfileResponse) }
                }
            }
        }
    }

}

sealed class ProfileShortVideoState {
    object Loading: ProfileShortVideoState()
    data class Success(val data: InfoProfileResponse): ProfileShortVideoState()
    data class Error(val message: String): ProfileShortVideoState()
}

