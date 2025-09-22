package com.nlhd.shortvideo.Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.shortVideo.Comments.GetComments.Comment
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.domain.entity.shortVideo.ProfileShortVideo.Info.InfoProfileResponse
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.shortvideo.ShortVideoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileShortVideoViewModel(
    private val shortVideoUseCase: ShortVideoUseCase
): ViewModel() {

    private var _state = MutableStateFlow<ProfileShortVideoState>(ProfileShortVideoState.Loading)
    val state = _state.asStateFlow()

    private var _followState = MutableStateFlow<FollowActionState>(FollowActionState.Idle)
    val followState = _followState.asStateFlow()

    private val tokenFlow = MutableStateFlow<String?>(null)
    private val videosFlows = mutableMapOf<Int, Flow<PagingData<Video>>>()

    fun setToken(token: String) {
        // tránh rebuild khi token không đổi
        if (tokenFlow.value != token) tokenFlow.value = token
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getVideosByUser(userId: Int) = videosFlows.getOrPut(userId) {
        tokenFlow.filterNotNull().flatMapLatest { token ->
            shortVideoUseCase.getVideosByUser(token, userId)
                .cachedIn(viewModelScope)
        }
    }

    fun getInfoProfile(token: String, userId: Int) = viewModelScope.launch {
        shortVideoUseCase.getInfoProfile(token, userId).let { result ->
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

    fun follow(token: String, userId: Int) = viewModelScope.launch {
        _followState.update { FollowActionState.Loading }
        shortVideoUseCase.follows(token, userId.toString()).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _followState.update { FollowActionState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _followState.update { FollowActionState.Success(result.value as MessageResponse) }
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

sealed class FollowActionState {
    object Idle: FollowActionState()
    object Loading: FollowActionState()
    data class Success(val data: MessageResponse): FollowActionState()
    data class Error(val message: String): FollowActionState()
}

