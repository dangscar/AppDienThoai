package com.nlhd.shortvideo.MyVideos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nlhd.domain.entity.Message.MessageResponse
import com.nlhd.domain.entity.shortVideo.GetVideos.Video
import com.nlhd.domain.resultWrapper.ResultWrapper
import com.nlhd.domain.usecase.shortvideo.ShortVideoUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyVideoViewModel(
    private val shortVideoUseCase: ShortVideoUseCase
): ViewModel() {
    private var _query = MutableStateFlow("")
    val query = _query.asStateFlow()
    private val trigger = MutableSharedFlow<String>(replay = 1,extraBufferCapacity = 1)
    private val tokenFlow = MutableStateFlow<String?>(null)

    private var _deleteVideoState = MutableStateFlow<MyVideoState>(MyVideoState.Idle)
    val deleteVideoState = _deleteVideoState.asStateFlow()

    private var _updateCaptionState = MutableStateFlow<MyVideoState>(MyVideoState.Idle)
    val updateCaptionState = _updateCaptionState.asStateFlow()

    private var _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private var _videoId = MutableStateFlow<Int?>(null)
    val videoId = _videoId.asStateFlow()

    private var _caption = MutableStateFlow<String?>(null)
    val caption = _caption.asStateFlow()

    fun setCaption(caption: String) = _caption.update { caption }

    fun setVideoId(id: Int) = _videoId.update { id }

    fun setShowDialog(show: Boolean) { _showDialog.update { show }}

    fun setDeleteVideoState(state: MyVideoState) {
        _deleteVideoState.update { state }
    }

    fun setToken(token: String) {
        // tránh rebuild khi token không đổi
        if (tokenFlow.value != token) tokenFlow.value = token
    }

    fun setUpdateCaptionState(state: MyVideoState)  = _updateCaptionState.update { state }

    fun updateCaption(token: String) = viewModelScope.launch {
        _updateCaptionState.update { MyVideoState.Loading }
        shortVideoUseCase.updateCaption(token, _videoId.value!!, _caption.value).let { result ->
            when (result) {
                is ResultWrapper.Failure -> {
                    _updateCaptionState.update { MyVideoState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _updateCaptionState.update { MyVideoState.Success(result.value as MessageResponse) }
                }
            }
        }
    }

    fun deleteVideo(token: String) = viewModelScope.launch {
        _deleteVideoState.update { MyVideoState.Loading }
        if (_videoId.value == null) {
            return@launch
        }
        shortVideoUseCase.deleteVideo(token, _videoId.value!!).let { result->
            when (result) {
                is ResultWrapper.Failure -> {
                    _deleteVideoState.update { MyVideoState.Error(result.exception.message.toString()) }
                }
                is ResultWrapper.Success<*> -> {
                    _deleteVideoState.update { MyVideoState.Success(result.value as MessageResponse) }
                }
            }
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val videos: Flow<PagingData<Video>> =
        combine(tokenFlow.filterNotNull(), trigger.debounce(500).distinctUntilChanged()) { token, keyword ->
            token to keyword
        }.flatMapLatest { (token, keyword) ->
            shortVideoUseCase.getMyVideos(token, keyword) // repo trả về Pager.flow
        }.cachedIn(viewModelScope)

    fun onSearchClick() {
        viewModelScope.launch {
            trigger.emit(_query.value)
        }
    }

    fun setQuery(query: String) {
        _query.update { query }
    }

}

sealed class MyVideoState {
    object Idle: MyVideoState()
    object Loading: MyVideoState()
    data class Success(val data: MessageResponse): MyVideoState()
    data class Error(val message: String): MyVideoState()
}

